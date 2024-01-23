package com.example.module_ads.domain.repositories

import android.app.Activity
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd

interface NativeAdRepository {
    fun loadNativeAd(activity: Activity,adUnitId: String, nativeAdLoadCallback: NativeAdLoadCallback)
    fun getNativeAd(): NativeAd?
    fun populateNativeAdView()
    fun destroyNativeAd()

    interface NativeAdLoadCallback {
        fun onNativeAdLoaded() {}
        fun onNativeAdFailedToLoad(loadAdError: LoadAdError) {}
        fun onNativeAdImpression() {}
        fun onNativeAdClicked() {}
        fun onNativeAdNotAvailable() {}
    }
}