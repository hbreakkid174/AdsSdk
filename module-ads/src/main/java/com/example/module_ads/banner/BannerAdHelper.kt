@file:Suppress("DEPRECATION")

package com.example.module_ads.banner

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.module_ads.enums.CollapsibleBannerPosition
import com.example.module_ads.views.debug
import com.example.module_ads.views.isNetworkAvailable
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*
import javax.inject.Inject

class BannerAdHelper @Inject constructor() : LifecycleObserver {

    private var adView: AdView? = null

    /**
     * Loads banner ads based on the provided parameters.
     *
     * @param activity The activity where the banner ad is loaded.
     * @param bannerContainerView The FrameLayout where the banner ad will be displayed.
     * @param adUnitId The ad unit ID for the AdMob adaptive banner.
     * @param collapsibleBannerPosition The position of a collapsible banner (default is CollapsibleBannerPosition.NONE).
     */
    fun loadBannerAds(
        activity: Activity?,
        bannerContainerView: FrameLayout,
        adUnitId: String,
        collapsibleBannerPosition: CollapsibleBannerPosition = CollapsibleBannerPosition.NONE
    ) {
        activity?.let { mActivity ->
            try {
                if (!mActivity.isNetworkAvailable()) {
                    debug("No internet connection found")
                    return
                }

                if (adView == null) {


                    // Set up the AdView for the adaptive banner
                    bannerContainerView.visibility = View.VISIBLE
                    adView = AdView(mActivity)
                    adView?.adUnitId = adUnitId
                    adView?.setAdSize(getAdSize(mActivity, bannerContainerView))

                    // Build the AdRequest based on the collapsible banner position
                    val adRequest: AdRequest = when (collapsibleBannerPosition) {
                        CollapsibleBannerPosition.NONE -> {
                            AdRequest.Builder().build()
                        }

                        CollapsibleBannerPosition.BOTTOM -> {
                            AdRequest.Builder()
                                .addNetworkExtrasBundle(AdMobAdapter::class.java, Bundle().apply {
                                    putString("collapsible", "bottom")
                                })
                                .build()
                        }

                        CollapsibleBannerPosition.TOP -> {
                            AdRequest.Builder()
                                .addNetworkExtrasBundle(AdMobAdapter::class.java, Bundle().apply {
                                    putString("collapsible", "top")
                                })
                                .build()
                        }
                    }

                    // Load the ad and set up the ad listener
                    adView?.loadAd(adRequest)
                    adView?.adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            debug("Banner ad loaded")
                            displayBannerAd(bannerContainerView)
                        }

                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            debug("Banner ad failed to load")
                            bannerContainerView.visibility = View.GONE
                        }

                        override fun onAdImpression() {
                            debug("Banner ad impression")
                            super.onAdImpression()
                        }

                        override fun onAdClicked() {
                            debug("Banner ad clicked")
                            super.onAdClicked()
                        }

                        override fun onAdClosed() {
                            debug("Banner ad closed")
                            super.onAdClosed()
                        }
                    }
                }
            } catch (ex: Exception) {
                // Handle exceptions, hide views if there is an issue
                bannerContainerView.removeAllViews()
                bannerContainerView.visibility = View.GONE
            }
        }
    }

    /**
     * Displays the banner ad by adding it to the provided FrameLayout.
     *
     * @param bannerContainerView The FrameLayout where the banner ad will be displayed.
     */
    private fun displayBannerAd(bannerContainerView: FrameLayout) {
        try {
            if (adView != null) {
                val viewGroup: ViewGroup? = adView?.parent as ViewGroup?
                viewGroup?.removeView(adView)

                bannerContainerView.removeAllViews()
                bannerContainerView.addView(adView)
            } else {
                // Hide views if the ad view is null
                bannerContainerView.removeAllViews()
                bannerContainerView.visibility = View.GONE
            }
        } catch (ex: Exception) {
            debug("inflateBannerAd: ${ex.message}")
        }
    }

    /**
     * Resumes the banner ad when the corresponding lifecycle event is triggered.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        debug("Banner resume")
        adView?.resume()
    }

    /**
     * Pauses the banner ad when the corresponding lifecycle event is triggered.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        debug("Banner is paused")
        adView?.pause()
    }

    /**
     * Destroys the banner ad when the corresponding lifecycle event is triggered.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        debug("Banner ad is destroyed")
        adView?.destroy()
        adView = null
    }

    /**
     * Gets the appropriate AdSize for the adaptive banner based on the container's width.
     *
     * @param mActivity The activity.
     * @param adContainer The FrameLayout where the banner ad will be displayed.
     * @return The calculated AdSize for the adaptive banner.
     */
    private fun getAdSize(mActivity: Activity, adContainer: FrameLayout): AdSize {
        val display = mActivity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = outMetrics.density

        var adWidthPixels = adContainer.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mActivity, adWidth)
    }
}
