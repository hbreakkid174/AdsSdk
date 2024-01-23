package com.example.module_ads.domain.usecases

import android.app.Activity
import android.widget.FrameLayout
import com.example.module_ads.domain.repositories.NativeAdRepository
import com.example.module_ads.enums.NativeAdType
import com.google.android.gms.ads.nativead.NativeAd
import javax.inject.Inject

class NativeAdUseCase @Inject constructor(
    private val nativeAdRepository: NativeAdRepository
) {
    fun getNativeAd(): NativeAd? {
        return nativeAdRepository.getNativeAd()
    }

    fun loadNativeAd(
        activity: Activity,
        adUnitId: String,
        nativeAdLoadCallback: NativeAdRepository.NativeAdLoadCallback
    ) {
        nativeAdRepository.loadNativeAd(activity, adUnitId, nativeAdLoadCallback)
    }

    fun populateNativeAdView(activity: Activity, nativeAdContainer: FrameLayout, nativeAdType: NativeAdType) {
        nativeAdRepository.populateNativeAdView(activity, nativeAdContainer, nativeAdType)
    }

    fun destroyNativeAd() {
        nativeAdRepository.destroyNativeAd()
    }
}