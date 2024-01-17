package com.example.module_ads.data

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import com.example.module_ads.domain.BannerAdRepository
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

/**
 * Implementation of [BannerAdRepository] responsible for loading and returning banner ads.
 */
class BannerAdRepositoryImpl @Inject constructor(private val context: Context) :
    BannerAdRepository {

    // Declare the AdView variable
    private var adView: AdView? = null

    // Determine the screen width (less decorations) to use for the ad width.
    // If the ad hasn't been laid out, default to the full screen width.
    private fun getAdSize(view: View): AdSize {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        val display = windowManager?.defaultDisplay
        val outMetrics = DisplayMetrics()
        display?.getMetrics(outMetrics)

        val density = outMetrics.density

        var adWidthPixels = view.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
    }

    /**
     * Loads a banner ad with the specified ad unit ID.
     *
     * @param adUnitId The ad unit ID of the banner ad.
     * @param adLoadCallback Callback to handle ad loading events.
     * @param view defines the view to calculate the banner ad size
     */
    override fun loadBannerAd(
        adUnitId: String,
        view: View,
        adLoadCallback: BannerAdRepository.BannerAdLoadCallback
    ) {
        if (!context.isNetworkAvailable()) {
            debug("Banner Ad is not available due to network error")
            adLoadCallback.onBannerAdNotAvailable()
            return
        }
        // Initialize the AdView
        if (adView == null) {
            adView = AdView(context)
            adView?.adUnitId = adUnitId
            adView?.setAdSize(getAdSize(view))

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
        }
    }

    /**
     * Returns the loaded banner ad if available.
     *
     * @return The loaded AdView representing the banner ad, or null if not loaded.
     */
    override fun returnBannerAd(): AdView? = adView

    /**
     * Releases the reference to the banner ad instance.
     * This is typically done when the ad is no longer needed.
     */
    override fun destroyBannerAd() {
        adView?.destroy()
        adView = null
    }

    /**
     * Loads a collapsible banner ad and handles various states using the provided callback.
     *
     * @param adUnitId The ad unit ID to identify the specific banner ad.
     * @param view The View where the banner ad will be displayed.
     * @param collapsibleBannerPosition The position where the collapsible banner will be displayed (TOP or BOTTOM).
     * @param adLoadCallback Callback to handle the states of the banner ad loading process.
     */
    override fun loadCollapsibleBanner(
        adUnitId: String,
        view: View,
        collapsibleBannerPosition: CollapsibleBannerPosition,
        adLoadCallback: BannerAdRepository.BannerAdLoadCallback
    ) {
        // Check if the network is available before attempting to load the banner ad.
        if (!context.isNetworkAvailable()) {
            debug("Collapsible Banner Ad is not available due to network error")
            adLoadCallback.onBannerAdNotAvailable()
            return
        }

        // Initialize the AdView if it hasn't been initialized yet.
        if (adView == null) {
            adView = AdView(context)

            // Set the ad unit ID and ad size for the AdView.
            adView?.adUnitId = adUnitId
            adView?.setAdSize(getAdSize(view))

            // Create an ad request based on the collapsible banner position.
//            val adRequest = when (collapsibleBannerPosition) {
//                CollapsibleBannerPosition.TOP -> {
//                    AdRequest.Builder()
//                        .addNetworkExtrasBundle(AdMobAdapter::class.java, Bundle().apply {
//                            putString("collapsible", "top")
//                        })
//                        .build()
//                }
//                CollapsibleBannerPosition.BOTTOM -> {
//                    AdRequest.Builder()
//                        .addNetworkExtrasBundle(AdMobAdapter::class.java, Bundle().apply {
//                            putString("collapsible", "bottom")
//                        })
//                        .build()
//                }
//            }
            // Create an extra parameter that aligns the bottom of the expanded ad to
            // the bottom of the bannerView.
            val extras = Bundle()
            extras.putString("collapsible", "bottom")

            val adRequest = AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                .build()

            // Start loading the ad in the background.
            adView?.loadAd(adRequest)

            // Set up an ad listener to handle loading events.
            adView?.adListener = object : AdListener() {
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
        }
    }

    /**
     * Resumes the banner ad, allowing it to resume processing and displaying ad content.
     */
    override fun resumeBannerAd() {
        // Check if the adView is not null before attempting to resume.
        // This is to avoid potential issues if the adView was not initialized properly.
        adView?.resume()
    }

    /**
     * Pauses the banner ad, stopping it from processing and displaying ad content temporarily.
     * This is typically used when the activity or fragment hosting the ad is paused.
     */
    override fun pauseBannerAd() {
        // Check if the adView is not null before attempting to pause.
        // This is to avoid potential issues if the adView was not initialized properly.
        adView?.pause()
    }


}
