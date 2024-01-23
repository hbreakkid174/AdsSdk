package com.example.module_ads.adstates

import com.google.android.gms.ads.LoadAdError

sealed class NativeAdState {
    object AdLoaded : NativeAdState()
    data class AdFailedToLoad(val loadAdError: LoadAdError) : NativeAdState()
    object AdNotAvailable : NativeAdState()
    object AdClicked : NativeAdState()
    object AdImpression : NativeAdState()
}