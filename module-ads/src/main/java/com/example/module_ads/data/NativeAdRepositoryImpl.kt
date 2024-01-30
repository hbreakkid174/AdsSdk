package com.example.module_ads.data

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.module_ads.R
import com.example.module_ads.domain.repositories.NativeAdRepository
import com.example.module_ads.enums.NativeAdType
import com.example.module_ads.views.debug
import com.example.module_ads.views.isNetworkAvailable
import com.example.module_ads.views.toast
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
    // Native ad instance
    private var mNativeAd: NativeAd? = null

    /*
    Load native ad from the specified ad unit ID.

    @param activity: The hosting activity.
    @param adUnitId: The ID of the ad unit.
    @param nativeAdLoadCallback: Callback to handle ad loading events.
    */
    override fun loadNativeAd(
        activity: Activity,
        adUnitId: String,
        nativeAdLoadCallback: NativeAdRepository.NativeAdLoadCallback
    ) {
        // Check network availability
        if (!activity.isNetworkAvailable()) {
            nativeAdLoadCallback.onNativeAdNotAvailable()
            debug("Native Ad not available due to network unavailable")
            activity.toast("Native Ad not available due to network unavailable")
            return
        }

        // Check if ad is already loaded
        if (mNativeAd != null) {
            debug("Native Ad already loaded")
            activity.toast("Native Ad already loaded")
            nativeAdLoadCallback.onNativeAdLoaded()
        } else {
            // Build ad loader
            val builder: AdLoader.Builder = AdLoader.Builder(activity, adUnitId)

            // Set up callbacks for the loaded ad
            builder.forNativeAd { unifiedNativeAd: NativeAd? ->
                val activityDestroyed: Boolean = activity.isDestroyed
                if (activityDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                    unifiedNativeAd?.destroy()
                    return@forNativeAd
                }
                mNativeAd?.destroy()
                mNativeAd = unifiedNativeAd
            }

            // Configure video options
            val videoOptions =
                VideoOptions.Builder()
                    .setStartMuted(false).build()

            // Configure ad options
            val adOptions = com.google.android.gms.ads.nativead.NativeAdOptions.Builder()
                .setVideoOptions(videoOptions).build()

            builder.withNativeAdOptions(adOptions)

            // Set up the ad loader with listeners
            val adLoader = builder.withAdListener(object : AdListener() {
                override fun onAdImpression() {
                    super.onAdImpression()
                    nativeAdLoadCallback.onNativeAdImpression()
                    mNativeAd = null
                    debug("Native Ad Impression")
                    activity.toast("Native Ad Impression")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    nativeAdLoadCallback.onNativeAdFailedToLoad(loadAdError)
                    mNativeAd = null
                    debug("Native Ad Failed to load")
                    activity.toast("Native Ad Failed to load")
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    nativeAdLoadCallback.onNativeAdLoaded()
                    debug("Native Ad Loaded")
                    activity.toast("Native Ad Loaded")
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    nativeAdLoadCallback.onNativeAdClicked()
                    debug("Native Ad Clicked")
                    activity.toast("Native Ad Clicked")
                }
            }).withNativeAdOptions(
                com.google.android.gms.ads.nativead.NativeAdOptions.Builder()
                    .setAdChoicesPlacement(
                        NativeAdOptions.ADCHOICES_TOP_RIGHT
                    ).build()
            ).build()

            // Load the ad
            adLoader.loadAd(AdRequest.Builder().build())
        }
    }

    // Get the native ad instance
    override fun getNativeAd() = mNativeAd

    /*
    Populate the specified native ad view with the loaded ad content.

    @param activity: The hosting activity.
    @param nativeAdContainer: The container for the native ad view.
    @param nativeAdType: The type of native ad view (e.g., MEDIUM, NATIVE_BANNER, LARGE, SMALL).
    */
    override fun populateNativeAdView(
        activity: Activity,
        nativeAdContainer: FrameLayout,
        nativeAdType: NativeAdType
    ) {
        if (mNativeAd != null) {
            // Inflate the appropriate layout based on ad type
            val inflater = LayoutInflater.from(activity)
            val adView: NativeAdView = when (nativeAdType) {
                NativeAdType.MEDIUM -> inflater.inflate(
                    R.layout.native_ad_medium,
                    null
                ) as NativeAdView

                NativeAdType.NATIVE_BANNER -> inflater.inflate(
                    R.layout.native_ad_banner,
                    null
                ) as NativeAdView

                NativeAdType.LARGE -> inflater.inflate(
                    R.layout.native_ad_large,
                    null
                ) as NativeAdView

                NativeAdType.SMALL -> inflater.inflate(
                    R.layout.native_ad_small,
                    null
                ) as NativeAdView
            }

            // Remove existing views from the container
            val viewGroup: ViewGroup? = adView.parent as ViewGroup?
            viewGroup?.removeView(adView)
            nativeAdContainer.removeAllViews()

            // Add the ad view to the container
            nativeAdContainer.addView(adView)

            // Configure media view for certain ad types
            if (nativeAdType == NativeAdType.MEDIUM || nativeAdType == NativeAdType.LARGE) {
                val mediaView: MediaView = adView.findViewById(R.id.media_view)
                adView.mediaView = mediaView
                if (nativeAdType == NativeAdType.LARGE) {
                    adView.mediaView?.setImageScaleType(ImageView.ScaleType.FIT_XY)
                }
            }

            // Set up views for headline, body, call to action, icon, etc.
            adView.headlineView = adView.findViewById(R.id.ad_headline)
            adView.bodyView = adView.findViewById(R.id.body)
            adView.callToActionView = adView.findViewById(R.id.cta)
            adView.iconView = adView.findViewById(R.id.icon)

            // Populate views with ad content
            adView.headlineView?.let { headline ->
                (headline as TextView).text = mNativeAd?.headline
                headline.isSelected = true
            }

            adView.callToActionView?.let { ctaView ->
                if (mNativeAd?.callToAction == null) {
                    ctaView.visibility = View.INVISIBLE
                } else {
                    ctaView.visibility = View.VISIBLE
                    (ctaView as Button).text = mNativeAd?.callToAction
                }
            }

            adView.bodyView?.let { bodyView ->
                if (mNativeAd?.body == null) {
                    bodyView.visibility = View.INVISIBLE
                } else {
                    bodyView.visibility = View.VISIBLE
                    (bodyView as TextView).text = mNativeAd?.body
                }
            }

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

    /*
    Destroy the native ad instance.
    */
    override fun destroyNativeAd() {
        debug("native ad is destroyed")
        mNativeAd?.destroy()
        mNativeAd = null
    }
}
