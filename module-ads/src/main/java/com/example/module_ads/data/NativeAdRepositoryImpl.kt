package com.example.module_ads.data

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.module_ads.R
import com.example.module_ads.domain.repositories.NativeAdRepository
import com.example.module_ads.enums.NativeAdType
import com.example.module_ads.views.debug
import com.example.module_ads.views.isNetworkAvailable
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
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
    override fun populateNativeAdView(
        activity: Activity,
        nativeAdContainer: FrameLayout,
        nativeAdType: NativeAdType
    ) {
        if (mNativeAd != null) {
            val inflater = LayoutInflater.from(activity)
            val adView: NativeAdView = when (nativeAdType) {
                NativeAdType.MEDIUM -> inflater.inflate(
                    R.layout.native_ad_medium,
                    null
                ) as NativeAdView

                NativeAdType.NATIVE_BANNER -> inflater.inflate(
                    R.layout.native_ad_medium,
                    null
                ) as NativeAdView

                NativeAdType.LARGE -> inflater.inflate(
                    R.layout.native_ad_medium,
                    null
                ) as NativeAdView

                NativeAdType.SMALL -> inflater.inflate(
                    R.layout.native_ad_medium,
                    null
                ) as NativeAdView
            }
            val viewGroup: ViewGroup? = adView.parent as ViewGroup?
            viewGroup?.removeView(adView)

            nativeAdContainer.removeAllViews()
            nativeAdContainer.addView(adView)
            if (nativeAdType == NativeAdType.MEDIUM) {
                val mediaView: MediaView = adView.findViewById(R.id.media_view)
                adView.mediaView = mediaView
            }
            adView.headlineView = adView.findViewById(R.id.ad_headline)
            adView.bodyView = adView.findViewById(R.id.body)
            adView.callToActionView = adView.findViewById(R.id.cta)
            adView.iconView = adView.findViewById(R.id.icon)
            //Headline
            adView.headlineView?.let { headline ->
                (headline as TextView).text = mNativeAd?.headline
                headline.isSelected = true
            }

            //Body
            adView.bodyView?.let { bodyView ->
                if (mNativeAd?.body == null) {
                    bodyView.visibility = View.INVISIBLE
                } else {
                    bodyView.visibility = View.VISIBLE
                    (bodyView as TextView).text = mNativeAd?.body
                }

            }
            //Icon
            adView.iconView?.let { iconView ->
                if (mNativeAd?.icon == null) {
                    iconView.visibility = View.GONE
                } else {
                    (iconView as ImageView).setImageDrawable(mNativeAd?.icon?.drawable)
                    iconView.visibility = View.VISIBLE
                }

            }

            adView.advertiserView?.let { adverView ->

                if (mNativeAd?.advertiser == null) {
                    adverView.visibility = View.GONE
                } else {
                    (adverView as TextView).text = mNativeAd?.advertiser
                    adverView.visibility = View.GONE
                }
            }
            adView.setNativeAd(mNativeAd ?: return)

        } else {
            debug("native ad is null")
        }
    }


    override fun destroyNativeAd() {
        debug("native ad is destroyed")
        mNativeAd?.destroy()
        mNativeAd = null
    }
}