package com.example.module_ads.data.model

import com.google.android.gms.ads.interstitial.InterstitialAd

data class InterstitialAdInfo(
    val adKey:Int,
    val adName:String,
    val adUnitId:String,
    val isRemoteConfig:Boolean=false,
    var isAdsLoading:Boolean=false,
    var interstitialAd: InterstitialAd?=null
)
