package com.example.module_ads

sealed class AdMobAdState {
        object AdLoaded : AdMobAdState()
        data class AdFailedToLoad(val errorCode: Int) : AdMobAdState()
    }