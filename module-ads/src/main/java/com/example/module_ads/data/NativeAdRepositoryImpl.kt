package com.example.module_ads.data

import android.app.Activity
import android.util.Log
import android.view.View
import com.example.module_ads.domain.repositories.NativeAdRepository
import com.example.module_ads.views.debug
import com.example.module_ads.views.isNetworkAvailable
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAd
import javax.inject.Inject

class NativeAdRepositoryImpl @Inject constructor() : NativeAdRepository {
    private var mNativeAd: NativeAd? = null
    override fun loadNativeAd(
        activity: Activity,
        adUnitId: String,
        nativeAdLoadCallback: NativeAdRepository.NativeAdLoadCallback
    ) {
        if (!activity.isNetworkAvailable()) {
            nativeAdLoadCallback.onNativeAdNotAvailable()
            debug("Native Ad not available due to network unavailable")
            return
        }
        if (mNativeAd != null) {
            debug("Native Ad already loaded")

            nativeAdLoadCallback.onNativeAdLoaded()
        } else {
            val builder: AdLoader.Builder = AdLoader.Builder(activity, adUnitId)
            builder.forNativeAd { unifiedNativeAd: NativeAd? ->
                val activityDestroyed: Boolean = activity.isDestroyed
                if (activityDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                    unifiedNativeAd?.destroy()
                    return@forNativeAd
                }
                mNativeAd?.destroy()
                mNativeAd = unifiedNativeAd
            }
            val videoOptions =
                VideoOptions.Builder()
                    .setStartMuted(false).build()

            val adOptions = com.google.android.gms.ads.nativead.NativeAdOptions.Builder()
                .setVideoOptions(videoOptions).build()
            builder.withNativeAdOptions(adOptions)
            val adLoader = builder.withAdListener(object : AdListener() {
                override fun onAdImpression() {
                    super.onAdImpression()
                    nativeAdLoadCallback.onNativeAdImpression()
                    mNativeAd = null
                    debug("Native Ad Impression")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    nativeAdLoadCallback.onNativeAdFailedToLoad(loadAdError)
                    mNativeAd = null
                    debug("Native Ad Failed to load")
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    nativeAdLoadCallback.onNativeAdLoaded()
                    debug("Native Ad Loaded")

                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    nativeAdLoadCallback.onNativeAdClicked()
                    debug("Native Ad Clicked")
                }

            }).withNativeAdOptions(
                com.google.android.gms.ads.nativead.NativeAdOptions.Builder()
                    .setAdChoicesPlacement(
                        NativeAdOptions.ADCHOICES_TOP_RIGHT
                    ).build()
            )
                .build()
            adLoader.loadAd(AdRequest.Builder().build())
        }

    }

    override fun getNativeAd() = mNativeAd

    override fun populateNativeAdView() {

    }

    override fun destroyNativeAd() {
        mNativeAd?.destroy()
        mNativeAd = null
    }
}