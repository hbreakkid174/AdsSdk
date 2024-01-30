package com.example.module_ads.domain.repositories

import android.app.Activity
import android.widget.FrameLayout
import com.example.module_ads.enums.NativeAdType
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd


/**
 * Repository interface for managing Native Ads.
 */
interface NativeAdRepository {

    /**
     * Loads a native ad for the given [activity], [adUnitId], and [nativeAdLoadCallback].
     *
     * @param activity The activity context.
     * @param adUnitId The ad unit ID for loading the native ad.
     * @param nativeAdLoadCallback Callback to receive events related to native ad loading.
     */
    fun loadNativeAd(activity: Activity, adUnitId: String, nativeAdLoadCallback: NativeAdLoadCallback)

    /**
     * Retrieves the currently loaded native ad.
     *
     * @return The currently loaded native ad, or null if no ad is loaded.
     */
    fun getNativeAd(): NativeAd?

    /**
     * Populates the native ad view within the specified [nativeAdContainer] for the given [nativeAdType].
     *
     * @param activity The activity context.
     * @param nativeAdContainer The container to populate with the native ad view.
     * @param nativeAdType The type of native ad layout to use.
     */
    fun populateNativeAdView(activity: Activity, nativeAdContainer: FrameLayout, nativeAdType: NativeAdType)

    /**
     * Destroys the currently loaded native ad.
     */
    fun destroyNativeAd()

    /**
     * Callback interface for events related to native ad loading.
     */
    interface NativeAdLoadCallback {

        /**
         * Called when a native ad has been successfully loaded.
         */
        fun onNativeAdLoaded()

        /**
         * Called when loading a native ad has failed.
         *
         * @param loadAdError Details about the failed ad load.
         */
        fun onNativeAdFailedToLoad(loadAdError: LoadAdError)

        /**
         * Called when a native ad impression is detected.
         */
        fun onNativeAdImpression(){}

        /**
         * Called when the user clicks on the native ad.
         */
        fun onNativeAdClicked(){}

        /**
         * Called when a native ad is not available.
         */
        fun onNativeAdNotAvailable()
    }
}
