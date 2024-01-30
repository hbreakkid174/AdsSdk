package com.example.module_ads.domain.usecases

import android.app.Activity
import android.widget.FrameLayout
import com.example.module_ads.domain.repositories.NativeAdRepository
import com.example.module_ads.enums.NativeAdType
import com.google.android.gms.ads.nativead.NativeAd
import javax.inject.Inject

/**
 * Use case class for managing interactions with Native Ads.
 *
 * @param nativeAdRepository The repository responsible for handling Native Ads.
 */
class NativeAdUseCase @Inject constructor(
    private val nativeAdRepository: NativeAdRepository
) {

    /**
     * Retrieves the currently loaded native ad.
     *
     * @return The currently loaded native ad, or null if no ad is loaded.
     */
    fun getNativeAd(): NativeAd? {
        return nativeAdRepository.getNativeAd()
    }

    /**
     * Loads a native ad for the given [activity], [adUnitId], and [nativeAdLoadCallback].
     *
     * @param activity The activity context.
     * @param adUnitId The ad unit ID for loading the native ad.
     * @param nativeAdLoadCallback Callback to receive events related to native ad loading.
     */
    fun loadNativeAd(
        activity: Activity,
        adUnitId: String,
        nativeAdLoadCallback: NativeAdRepository.NativeAdLoadCallback
    ) {
        nativeAdRepository.loadNativeAd(activity, adUnitId, nativeAdLoadCallback)
    }

    /**
     * Populates the native ad view within the specified [nativeAdContainer] for the given [nativeAdType].
     *
     * @param activity The activity context.
     * @param nativeAdContainer The container to populate with the native ad view.
     * @param nativeAdType The type of native ad layout to use.
     */
    fun populateNativeAdView(activity: Activity, nativeAdContainer: FrameLayout, nativeAdType: NativeAdType) {
        nativeAdRepository.populateNativeAdView(activity, nativeAdContainer, nativeAdType)
    }

    /**
     * Destroys the currently loaded native ad.
     */
    fun destroyNativeAd() {
        nativeAdRepository.destroyNativeAd()
    }
}