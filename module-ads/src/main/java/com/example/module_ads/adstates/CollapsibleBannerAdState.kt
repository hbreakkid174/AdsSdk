
package com.example.module_ads.adstates

sealed class CollapsibleBannerAdState {
    object AdLoaded : CollapsibleBannerAdState()
    data class AdFailedToLoad(val errorCode: Int) : CollapsibleBannerAdState()
    object AdNotAvailable : CollapsibleBannerAdState()
}
