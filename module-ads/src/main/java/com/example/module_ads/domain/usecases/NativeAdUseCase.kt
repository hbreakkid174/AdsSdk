package com.example.module_ads.domain.usecases

import android.app.Activity
import com.example.module_ads.domain.repositories.NativeAdRepository
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

    fun populateNativeAdView() {
        nativeAdRepository.populateNativeAdView()
    }

    fun destroyNativeAd() {
        nativeAdRepository.destroyNativeAd()
    }
}