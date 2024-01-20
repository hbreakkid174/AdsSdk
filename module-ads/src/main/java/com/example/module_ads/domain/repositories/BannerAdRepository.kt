package com.example.module_ads.domain.repositories

import android.content.Context
import android.view.View
import com.example.module_ads.enums.CollapsibleBannerPosition
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
     * @param view defines the view to calculate the banner ad size
     */
    fun loadBannerAd(context: Context,adUnitId: String, view: View, adLoadCallback: BannerAdLoadCallback)

    /**
     * Returns the loaded banner ad if available.
     *
     * @return The loaded AdView representing the banner ad, or null if not loaded.
     */
    fun returnBannerAd(): AdView?
    fun returnCollapsedBannerAd(): AdView?


    /**
     * Loads a collapsible banner ad and handles various states using the provided callback.
     *
     * @param adUnitId The ad unit ID to identify the specific banner ad.
     * @param view The View where the banner ad will be displayed.
     * @param collapsibleBannerPosition The position where the collapsible banner will be displayed (TOP or BOTTOM).
     * @param adLoadCallback Callback to handle the states of the banner ad loading process.
     */
    fun loadCollapsibleBanner(
        context: Context,
        adUnitId: String,
        view: View,
        collapsibleBannerPosition: CollapsibleBannerPosition,
        adLoadCallback: BannerAdLoadCallback
    )

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
        /**
         * Callback triggered when the ad is not available means internet connection is not found
         * App is purchased
         *
         */
        fun onBannerAdNotAvailable()
    }
}