package com.example.module_ads.adstates

import com.google.android.gms.ads.AdError

sealed class InterstitialAdState {
    object AdLoaded : InterstitialAdState()
    data class AdFailedToLoad(val errorCode: Int) : InterstitialAdState()
    object AdNotAvailable : InterstitialAdState()
    object AdDismissed : InterstitialAdState()
    object AdClicked : InterstitialAdState()
    object AdImpression : InterstitialAdState()
    object AdShowed : InterstitialAdState()
    data class AdFailedToShowFullScreenContent(val adError: AdError) : InterstitialAdState()
}
