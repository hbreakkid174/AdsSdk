package com.example.module_ads.data.data

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.module_ads.data.model.InterstitialAdInfo
import com.example.module_ads.domain.repositories.InterstitialAdRepository
import com.example.module_ads.utils.FullScreenDialog
import com.example.module_ads.views.debug
import com.example.module_ads.views.isNetworkAvailable
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import javax.inject.Inject

/**
 * Implementation of the [InterstitialAdRepository] interface for managing and displaying interstitial ads.
 *
 * @property context The application context used for network availability and loading ads.
 */
class InterstitialAdRepositoryImpl @Inject constructor(
    private val context: Context
) : InterstitialAdRepository {



    // Dialog to display while the ad is loading.
    private var fullScreenDialog: FullScreenDialog? = null

    // Cache to store loaded interstitial ads with their respective keys.
    private val adInterstitialRepo = HashMap<Int, InterstitialAdInfo>()

    /**
     * Loads a standard interstitial ad for the given [adInfo].
     *
     * @param adInfo Information about the interstitial ad to be loaded.
     * @param isPurchased Indicates if the user has made a purchase, affecting ad display.
     * @param callback Callback to notify when the ad is loaded or fails.
     */
    override fun loadNormalInterstitialAd(
        adInfo: InterstitialAdInfo,
        isPurchased: Boolean,
        callback: InterstitialAdRepository.InterstitialAdLoadCallback
    ) {
        if (!context.isNetworkAvailable()) {
            debug("Ad is not available due to network error")
            callback.onInterstitialAdNotAvailable()
            return
        }
        if (!adInfo.isRemoteConfig) {
            debug("Ad is not available due to remote config error")
            callback.onInterstitialAdNotAvailable()
            return
        }
        if (isPurchased) {
            debug("Ad is not available due to purchase error")
            callback.onInterstitialAdNotAvailable()
            return
        }
        // Check if the ad is already loaded or in progress.
        val existingAdInfo = adInterstitialRepo[adInfo.adKey]
        if (existingAdInfo?.isAdsLoading == true || existingAdInfo?.interstitialAd != null) {
            debug("already loaded normalInterstitialAd")
            callback.onInterstitialAdLoaded()
            return
        }
        // Cache ad info and mark as loading.
        adInterstitialRepo[adInfo.adKey] = adInfo.apply { isAdsLoading = true }
        // Build and load the interstitial ad request.
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            adInfo.adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    val error = "domain: ${adError.domain}, code: ${adError.code}, message: ${adError.message}"
                    debug("onAdFailedToLoad() with error $error")
                    adInterstitialRepo[adInfo.adKey]?.isAdsLoading = false
                    adInterstitialRepo[adInfo.adKey]?.interstitialAd = null

                    callback.onInterstitialAdFailedToLoad(adError.code)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    debug("Ad was loaded.")
                    adInterstitialRepo[adInfo.adKey]?.apply {
                        this.interstitialAd = interstitialAd
                        isAdsLoading = false
                    }
                    callback.onInterstitialAdLoaded()
                }
            })


    }

    /**
     * Returns the interstitial ad instance associated with the given [adKey].
     *
     * @param adKey Unique key identifying the ad.
     * @return The [InterstitialAdInfo] instance or null if not found.
     */
    override fun returnNormalInterstitialAd(adKey: Int) = adInterstitialRepo[adKey]


    /**
     * Releases the reference to the loaded interstitial ad associated with the given [adKey].
     *
     * @param adKey Unique key identifying the ad to be released.
     */
    override fun releaseNormalInterstitialAd(adKey: Int) {
        adInterstitialRepo[adKey]?.interstitialAd = null
        adInterstitialRepo[adKey]?.isAdsLoading = false
    }

    /**
     * Displays a loaded interstitial ad.
     *
     * @param adInfo Information about the interstitial ad to be shown.
     * @param isPurchased Indicates if the user has made a purchase, affecting ad display.
     * @param activity The activity in which the ad will be shown.
     * @param interstitialAdLoadCallback Callback to notify about ad show events or errors.
     */
    override fun showNormalInterstitialAd(
        adInfo: InterstitialAdInfo,
        isPurchased: Boolean,
        activity: Activity,
        interstitialAdLoadCallback: InterstitialAdRepository.InterstitialAdLoadCallback
    ) {
        if (!context.isNetworkAvailable()) {
            debug("Ad is not available due to network error")
            interstitialAdLoadCallback.onInterstitialAdNotAvailable()
            return
        }
        if (!adInfo.isRemoteConfig) {
            debug("Ad is not available due to remote config error")
            interstitialAdLoadCallback.onInterstitialAdNotAvailable()
            return
        }
        if (isPurchased) {
            debug("Ad is not available due to purchase error")
            interstitialAdLoadCallback.onInterstitialAdNotAvailable()
            return
        }
        val interstitialAdInfo = adInterstitialRepo[adInfo.adKey]
        val interstitialAd = interstitialAdInfo?.interstitialAd
        if (interstitialAd != null) {
            fullScreenDialog = FullScreenDialog(activity).apply { show() }

            Handler(Looper.getMainLooper()).postDelayed({
                interstitialAd.show(activity)
            }, 1000)
            // Handle full-screen content callbacks.
            interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    debug("Ad was clicked")
                    interstitialAdLoadCallback.onInterstitialAdClicked()
                }

                override fun onAdDismissedFullScreenContent() {
                    debug("Ad dismissed fullscreen content")
                    adInterstitialRepo[adInfo.adKey]?.interstitialAd = null
                    fullScreenDialog?.dismiss()
                    fullScreenDialog = null
                    interstitialAdLoadCallback.onInterstitialAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    debug("Ad failed to show fullscreen content")

                    fullScreenDialog?.dismiss()
                    adInterstitialRepo[adInfo.adKey]?.interstitialAd = null
                    interstitialAdLoadCallback.onInterstitialAdFailedToShowFullScreenContent(adError)
                }

                override fun onAdImpression() {
                    debug("Ad recorded an impression")
                    interstitialAdLoadCallback.onInterstitialAdImpression()
                }

                override fun onAdShowedFullScreenContent() {
                    debug("Ad showed fullscreen content")
                    interstitialAdLoadCallback.onInterstitialAdShowed()
                }
            }
        }else{
            debug("Ad Not Available")
            interstitialAdLoadCallback.onInterstitialAdNotAvailable()
            return
        }
    }
}
