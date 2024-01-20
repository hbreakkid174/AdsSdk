package com.example.module_ads.adstates
sealed class BannerAdState {
    object AdLoaded : BannerAdState()
    data class AdFailedToLoad(val errorCode: Int) : BannerAdState()
    object AdNotAvailable : BannerAdState()
}
