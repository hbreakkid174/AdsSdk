package com.example.module_ads.domain.repositories

import android.app.Activity
import android.widget.FrameLayout
import com.example.module_ads.enums.NativeAdType
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd

interface NativeAdRepository {
    fun loadNativeAd(activity: Activity,adUnitId: String, nativeAdLoadCallback: NativeAdLoadCallback)
    fun getNativeAd(): NativeAd?
    fun populateNativeAdView(activity: Activity,nativeAdContainer: FrameLayout,nativeAdType: NativeAdType)
    fun destroyNativeAd()

    interface NativeAdLoadCallback {
        fun onNativeAdLoaded() {}
        fun onNativeAdFailedToLoad(loadAdError: LoadAdError) {}
        fun onNativeAdImpression() {}
        fun onNativeAdClicked() {}
        fun onNativeAdNotAvailable() {}
    }
}