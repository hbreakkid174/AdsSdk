package com.example.module_ads.open_ad

import android.app.Activity
import android.content.Context
import com.example.module_ads.utils.GoogleMobileAdsConsentManager
import com.example.module_ads.utils.debug
import com.example.module_ads.utils.toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Date

class OpenAdHelper(private val activity: Context, private val adUnitId: String) {
    private var appOpenAdManager: AppOpenAdManager
    fun getAppOpenAdManager()=appOpenAdManager

    init {
        appOpenAdManager = AppOpenAdManager()
    }

    var isShowingAd = appOpenAdManager.isShowingAd

    /**
     * Shows an app open ad.
     *
     * @param activity the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener)
    }

    /**
     * Load an app open ad.
     *
     * @param activity the activity that shows the app open ad
     */
    fun loadAd(activity: Activity) {
        // We wrap the loadAd to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.loadAd(activity)
    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete (i.e.
     * dismissed or fails to show).
     */
    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }

    /** Inner class that loads and shows app open ads. */
    inner class AppOpenAdManager {
        private var adsConsentManager: GoogleMobileAdsConsentManager =
            GoogleMobileAdsConsentManager.getInstance(activity)


        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false

        /** Keep track of the time an app open ad is loaded to ensure you don't show an expired ad. */
        private var loadTime: Long = 0

        /**
         * Load an ad.
         *
         * @param context the context of the activity that loads the ad
         */
        fun loadAd(context: Context) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return
            }

            isLoadingAd = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                context,
                adUnitId,
                request,
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                        debug("onAdLoaded.")
                        context.toast("onAdLoaded.")
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        isLoadingAd = false
                        debug("onAdFailedToLoad: " + loadAdError.message)
                        context.toast("onAdFailedToLoad")
                    }
                }
            )
        }

        /** Check if ad was loaded more than n hours ago. */
        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference: Long = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }

        /** Check if ad exists and can be shown. */
        private fun isAdAvailable(): Boolean {
            // Ad references in the app open beta will time out after four hours, but this time limit
            // may change in future beta versions. For details, see:
            // https://support.google.com/admob/answer/9341964?hl=en
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         */
        fun showAdIfAvailable(activity: Activity) {
            showAdIfAvailable(
                activity,
                object : OnShowAdCompleteListener {
                    override fun onShowAdComplete() {
                        // Empty because the user will go back to the activity that shows the ad.
                    }
                }
            )
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
         */
        fun showAdIfAvailable(
            activity: Activity,
            onShowAdCompleteListener: OnShowAdCompleteListener
        ) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                debug("The app open ad is already showing.")
                return
            }

            // If the app open ad is not available yet, invoke the callback.
            if (!isAdAvailable()) {
                debug("The app open ad is not ready yet.")
                onShowAdCompleteListener.onShowAdComplete()
                if (adsConsentManager?.canRequestAds == true) {
                    loadAd(activity)
                }
                return
            }

            debug("Will show ad.")

            appOpenAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    /** Called when full screen content is dismissed. */
                    override fun onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false
                        debug("onAdDismissedFullScreenContent.")
                        activity.toast("onAdDismissedFullScreenContent")

                        onShowAdCompleteListener.onShowAdComplete()
                        if (adsConsentManager?.canRequestAds == true) {
                            loadAd(activity)
                        }
                    }

                    /** Called when fullscreen content failed to show. */
                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        appOpenAd = null
                        isShowingAd = false
                        debug("onAdFailedToShowFullScreenContent: " + adError.message)
                        activity.toast("onAdFailedToShowFullScreenContent")

                        onShowAdCompleteListener.onShowAdComplete()
                        if (adsConsentManager?.canRequestAds == true) {
                            loadAd(activity)
                        }
                    }

                    /** Called when fullscreen content is shown. */
                    override fun onAdShowedFullScreenContent() {
                        debug("onAdShowedFullScreenContent.")
                        activity.toast("onAdShowedFullScreenContent")
                    }
                }
            isShowingAd = true
            appOpenAd?.show(activity)
        }
    }

}