package com.example.module_ads.domain.repositories

import android.app.Activity
import com.example.module_ads.data.model.InterstitialAdInfo
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.interstitial.InterstitialAd

/**
 * Interface defining operations for loading, showing, and managing interstitial ads.
 */
interface InterstitialAdRepository {

    /**
     * Loads an interstitial ad based on the provided [adInfo].
     *
     * @param adInfo The information about the interstitial ad to load (e.g., ad unit ID, ad key).
     * @param isPurchased Flag indicating whether the user has purchased a service or subscription
     *        that removes ads. If true, no ad will be loaded.
     * @param callback Callback interface for receiving the result of the ad loading process.
     */
    fun loadNormalInterstitialAd(
        adInfo: InterstitialAdInfo,
        isPurchased: Boolean = false,
        callback: InterstitialAdLoadCallback
    )

    /**
     * Returns the instance of the loaded interstitial ad associated with the provided [adKey].
     *
     * @param adKey The unique key associated with the loaded ad.
     * @return The [InterstitialAdInfo] instance containing the ad, or null if no ad is loaded.
     */
    fun returnNormalInterstitialAd(adKey: Int): InterstitialAdInfo?

    /**
     * Returns the instance of the loaded interstitial ad associated with the provided [adKey].
     *
     * @param adKey The unique key associated with the loaded ad.
     * @return The [InterstitialAdInfo] instance containing the ad, or null if no ad is loaded.
     */
    fun releaseNormalInterstitialAd(adKey: Int)

    /**
     * Displays a loaded interstitial ad in the specified [activity].
     *
     * @param adInfo The information about the interstitial ad to be shown.
     * @param isPurchased Flag indicating whether the user has purchased a service or subscription
     *        that removes ads. If true, the ad will not be shown.
     * @param activity The activity context in which the interstitial ad will be displayed.
     * @param interstitialAdLoadCallback Callback interface for handling ad show events and results.
     */
    fun showNormalInterstitialAd(
        adInfo: InterstitialAdInfo,
        isPurchased: Boolean = false,
        activity: Activity,
        interstitialAdLoadCallback: InterstitialAdLoadCallback
    )


    /**
     * Callback interface for handling interstitial ad loading, showing, and interaction events.
     */
    interface InterstitialAdLoadCallback {
        /**
         * Called when the interstitial ad has successfully loaded.
         */
        fun onInterstitialAdLoaded() {}
        /**
         * Called when the interstitial ad fails to load.
         *
         * @param errorCode The error code indicating the reason for the ad load failure.
         */
        fun onInterstitialAdFailedToLoad(errorCode: Int) {}

        /**
         * Called when the interstitial ad is unavailable due to conditions such as
         * lack of network connectivity, ad configuration issues, or user subscription (purchases).
         */
        fun onInterstitialAdNotAvailable() {}
        /**
         * Called when the interstitial ad is clicked by the user.
         */
        fun onInterstitialAdClicked() {}
        /**
         * Called when the interstitial ad is dismissed by the user.
         */
        fun onInterstitialAdDismissed() {}
        /**
         * Called when an impression is recorded for the interstitial ad, indicating
         * that the ad has been viewed by the user.
         */
        fun onInterstitialAdImpression() {}
        /**
         * Called when the interstitial ad has been successfully displayed to the user.
         */
        fun onInterstitialAdShowed() {}
        /**
         * Called when the interstitial ad fails to show its full-screen content, typically
         * due to an error in the ad display process.
         *
         * @param adError An instance of [AdError] containing details about the failure.
         */
        fun onInterstitialAdFailedToShowFullScreenContent(adError: AdError) {}


    }

}
