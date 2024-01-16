package com.example.module_ads.domain

import com.google.android.gms.ads.AdView

/**
 * Repository interface for managing banner ad operations.
 */
interface BannerAdRepository {

    /**
     * Loads a banner ad with the specified ad unit ID.
     *
     * @param adUnitId The ad unit ID of the banner ad.
     * @param adLoadCallback Callback to handle ad loading events.
     */
    fun loadBannerAd(adUnitId: String, adLoadCallback: BannerAdLoadCallback)

    /**
     * Returns the loaded banner ad if available.
     *
     * @return The loaded AdView representing the banner ad, or null if not loaded.
     */
    fun returnBannerAd(): AdView?
    /**
     * Releases the reference to the banner ad instance.
     * This is typically done when the ad is no longer needed.
     */

    fun destroyBannerAd()
    interface BannerAdLoadCallback {
        /**
         * Callback triggered when the ad is successfully loaded.
         */
        fun onBannerAdLoaded()

        /**
         * Callback triggered when the ad fails to load.
         *
         * @param errorCode The error code indicating the reason for failure.
         */
        fun onBannerAdFailedToLoad(errorCode: Int)
    }
}