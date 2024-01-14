package com.example.module_ads.utils

sealed class AdMobAdState {
        object AdLoaded : AdMobAdState()
        data class AdFailedToLoad(val errorCode: Int) : AdMobAdState()
    }