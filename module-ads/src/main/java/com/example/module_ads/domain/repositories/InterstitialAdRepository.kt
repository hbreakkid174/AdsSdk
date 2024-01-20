package com.example.module_ads.domain.repositories

import android.app.Activity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.interstitial.InterstitialAd

/**
 * Interface defining operations related to loading and managing interstitial ads.
 */
interface InterstitialAdRepository {

    /**
     * Loads a normal interstitial ad with the specified [adUnitId].
     *
     * @param adUnitId The ad unit ID to load the ad.
     * @param callback The callback to handle ad loading results.
     */
    fun loadNormalInterstitialAd(adUnitId: String, callback: InterstitialAdLoadCallback)

    /**
     * Returns the instance of the loaded normal interstitial ad.
     *
     * @return The instance of the loaded interstitial ad, or null if not loaded.
     */
    fun returnNormalInterstitialAd(): InterstitialAd?

    /**
     * Releases the reference to the normal interstitial ad instance.
     * This is typically done when the ad is no longer needed.
     */
    fun releaseNormalInterstitialAd()
    /**
     * Shows a normal interstitial ad.
     *
     * @param activity The activity where the ad will be displayed.
     * @param interstitialAdLoadCallback The callback to handle interstitialAd results.
     */
    fun showNormalInterstitialAd(activity: Activity,interstitialAdLoadCallback: InterstitialAdLoadCallback)

    /**
     * Callback interface to handle the results of ad loading.
     */
    interface InterstitialAdLoadCallback {
        /**
         * Callback triggered when the ad is successfully loaded.
         */
        fun onInterstitialAdLoaded(){}

        /**
         * Callback triggered when the ad fails to load.
         *
         * @param errorCode The error code indicating the reason for failure.
         */
        fun onInterstitialAdFailedToLoad(errorCode: Int){}

        /**
         * Callback triggered when the ad is not available means internet connection is not found
         * App is purchased
         *
         */
        fun onInterstitialAdNotAvailable()

        fun onInterstitialAdClicked(){}
        fun onInterstitialAdDismissed(){}
        fun onInterstitialAdImpression(){}
        fun onInterstitialAdShowed(){}
        fun onInterstitialAdFailedToShowFullScreenContent(adError: AdError){}


    }

}
