package com.example.module_ads.data.data

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.WindowMetrics
import com.example.module_ads.domain.repositories.BannerAdRepository
import com.example.module_ads.enums.CollapsibleBannerPosition
import com.example.module_ads.views.debug
import com.example.module_ads.views.isNetworkAvailable
import com.example.module_ads.views.toast
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [BannerAdRepository] responsible for loading and returning banner ads.
 */
@Singleton
class BannerAdRepositoryImpl @Inject constructor() : BannerAdRepository {

    // AdView variables for the main banner and collapsible banner
    private var adView: AdView? = null
    private var collapsedAdView: AdView? = null

    // Determine the screen width (less decorations) to use for the ad width.
    // If the ad hasn't been laid out, default to the full screen width.
    private fun getAdSize(view: View, context: Context): AdSize {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        val display = windowManager?.defaultDisplay
        val outMetrics = DisplayMetrics()
        display?.getMetrics(outMetrics)
        var adWidthPixels = view.width.toFloat()
        var windowMetrics: WindowMetrics? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowMetrics = windowManager?.currentWindowMetrics
        }
        val adWidth: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bounds = windowMetrics?.bounds

            // If the ad hasn't been laid out, default to the full screen width.
            if (adWidthPixels == 0f) {
                adWidthPixels = bounds?.width()?.toFloat()!!
            }

            val density = context.resources.displayMetrics.density
            adWidth = (adWidthPixels / density).toInt()
        } else {
            val density = outMetrics.density

            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            adWidth = (adWidthPixels / density).toInt()
        }

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
    }

    /**
     * Loads a banner ad with the specified ad unit ID.
     *
     * @param adUnitId The ad unit ID of the banner ad.
     * @param adLoadCallback Callback to handle ad loading events.
     * @param view defines the view to calculate the banner ad size
     * @param context to use context to initialize the banner ad
     */
    override fun loadBannerAd(
        context: Context,
        adUnitId: String,
        view: View,
        adLoadCallback: BannerAdRepository.BannerAdLoadCallback
    ) {

        if (!context.isNetworkAvailable()) {
            debug("Banner Ad is not available due to network error")
            context.toast("Banner Ad is not available due to network error")
            adLoadCallback.onBannerAdNotAvailable()
            return
        }

        // Initialize the AdView
        if (adView == null) {
            adView = AdView(context)
            adView?.adUnitId = adUnitId
            adView?.setAdSize(getAdSize(view, context))

            // Create an ad request.
            val adRequest = AdRequest.Builder().build()

            // Start loading the ad in the background.
            adView?.loadAd(adRequest)

            // Set up an ad listener to handle loading events
            adView?.adListener = object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Code to be executed when an ad request fails.
                    debug("banner ad failed to load")
                    context.toast("banner ad failed to load")
                    adLoadCallback.onBannerAdFailedToLoad(adError.code)
                }

                override fun onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    debug("banner ad loaded")
                    context.toast("banner ad loaded")
                    adLoadCallback.onBannerAdLoaded()
                }
            }
        } else {
            // Banner ad already loaded
            debug("banner ad already loaded")
            context.toast("banner ad already loaded")
            adLoadCallback.onBannerAdLoaded()
        }
    }

    /**
     * Returns the loaded banner ad if available.
     *
     * @return The loaded AdView representing the banner ad, or null if not loaded.
     */
    override fun returnBannerAd(): AdView? = adView

    /**
     * Returns the loaded collapsed banner ad if available.
     *
     * @return The loaded AdView representing the collapsed banner ad, or null if not loaded.
     */
    override fun returnCollapsedBannerAd(): AdView? = collapsedAdView

    /**
     * Loads a collapsible banner ad and handles various states using the provided callback.
     *
     * @param adUnitId The ad unit ID to identify the specific banner ad.
     * @param view The View where the banner ad will be displayed.
     * @param collapsibleBannerPosition The position where the collapsible banner will be displayed (TOP or BOTTOM).
     * @param adLoadCallback Callback to handle the states of the banner ad loading process.
     * @param context to use context to initialize the banner ad
     */
    override fun loadCollapsibleBanner(
        context: Context,
        adUnitId: String,
        view: View,
        collapsibleBannerPosition: CollapsibleBannerPosition,
        adLoadCallback: BannerAdRepository.BannerAdLoadCallback
    ) {

        // Check if the network is available before attempting to load the banner ad.
        if (!context.isNetworkAvailable()) {
            debug("Collapsible Banner Ad is not available due to network error")
            context.toast("Collapsible Banner Ad is not available due to network error")
            adLoadCallback.onBannerAdNotAvailable()
            return
        }

        // Initialize the AdView if it hasn't been initialized yet.
        if (collapsedAdView == null) {
            collapsedAdView = AdView(context)

            // Set the ad unit ID and ad size for the AdView.
            collapsedAdView?.adUnitId = adUnitId
            collapsedAdView?.setAdSize(getAdSize(view, context))

            // Create an ad request based on the collapsible banner position.
            val adRequest = when (collapsibleBannerPosition) {
                CollapsibleBannerPosition.TOP -> {
                    AdRequest.Builder()
                        .addNetworkExtrasBundle(AdMobAdapter::class.java, Bundle().apply {
                            putString("collapsible", "top")
                        }).build()
                }

                CollapsibleBannerPosition.BOTTOM -> {
                    AdRequest.Builder()
                        .addNetworkExtrasBundle(AdMobAdapter::class.java, Bundle().apply {
                            putString("collapsible", "bottom")
                        }).build()
                }

            }

            // Start loading the ad in the background.
            collapsedAdView?.loadAd(adRequest)

            // Set up an ad listener to handle loading events.
            collapsedAdView?.adListener = object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Code to be executed when an ad request fails.
                    debug("Collapsible banner ad failed to load")
                    context.toast("Collapsible banner ad failed to load")
                    adLoadCallback.onBannerAdFailedToLoad(adError.code)
                }

                override fun onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    debug("Collapsible banner ad loaded")
                    context.toast("Collapsible banner ad loaded")
                    adLoadCallback.onBannerAdLoaded()
                }
            }
        } else {
            debug("Collapsible banner ad already loaded")
            context.toast("Collapsible banner ad already loaded")
            adLoadCallback.onBannerAdLoaded()
        }
    }

    /**
     * Resumes the banner ad.
     */
    override fun resumeBannerAd() {
        debug("Resuming banner")
        adView?.resume()
    }

    /**
     * Pauses the banner ad.
     */
    override fun pauseBannerAd() {
        debug("pauseBannerAd")
        adView?.pause()
    }

    /**
     * Destroys the banner ad.
     */
    override fun destroyBannerAd() {
        debug("Destroy banner ad")
        adView?.destroy()
        adView = null
    }

    /**
     * Resumes the collapsible banner ad.
     */
    override fun resumeCollapsibleBanner() {
        debug("resumeCollapsibleBanner")
        collapsedAdView?.resume()
    }

    /**
     * Pauses the collapsible banner ad.
     */
    override fun pauseCollapsibleBanner() {
        debug("pauseCollapsibleBanner")
        collapsedAdView?.pause()
    }

    /**
     * Destroys the collapsible banner ad.
     */
    override fun destroyCollapsibleBanner() {
        debug("destroy collapsible banner")
        collapsedAdView?.destroy()
        collapsedAdView = null
    }
}