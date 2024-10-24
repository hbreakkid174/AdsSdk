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
import com.example.module_ads.views.toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import javax.inject.Inject

/**
 * Implementation of the [InterstitialAdRepository] interface.
 *
 * @property context The application context.
 * @constructor Injected constructor to provide the application context.
 */
class InterstitialAdRepositoryImpl @Inject constructor(
    private val context: Context
) : InterstitialAdRepository {


    // The full-screen dialog to show while loading the ad.
    private var fullScreenDialog: FullScreenDialog? = null

    private val adInterstitialARepo = HashMap<Int, InterstitialAdInfo>()

    /**
     * Load a normal interstitial ad with the specified [adUnitId].
     *
     * @param adUnitId The ad unit ID to load the ad.
     * @param callback The callback to handle ad loading results.
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
        if (adInterstitialARepo.containsKey(adInfo.adKey)) {
            // Request a new ad if one isn't already loaded.
            if (adInterstitialARepo[adInfo.adKey]?.isAdsLoading == true || adInterstitialARepo[adInfo.adKey]?.interstitialAd != null) {
                debug("already loaded normalInterstitialAd")
                callback.onInterstitialAdLoaded()
                return
            }
        } else {
            adInterstitialARepo[adInfo.adKey] = adInfo
            adInterstitialARepo[adInfo.adKey]?.isAdsLoading = true

            // Build an AdRequest to load the interstitial ad.
            val adRequest = AdRequest.Builder().build()

            // Load the interstitial ad.
            InterstitialAd.load(
                context,
                adInfo.adUnitId,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    // Callback triggered when ad fails to load.
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        val error =
                            "domain: ${adError.domain}, code: ${adError.code}, " + "message: ${adError.message}"
                        debug("onAdFailedToLoad() with error $error")
                        context.toast("onAdFailedToLoad() with error $error")
                        // Set the ad instance to null.
                        adInterstitialARepo[adInfo.adKey]?.interstitialAd = null
                        adInterstitialARepo[adInfo.adKey]?.isAdsLoading = false
                        // Invoke the callback with the error code.
                        callback.onInterstitialAdFailedToLoad(adError.code)
                    }

                    // Callback triggered when ad is successfully loaded.
                    override fun onAdLoaded(interstitialAd1: InterstitialAd) {
                        debug("Ad was loaded.")
                        context.toast("Ad was loaded.")
                        // Set the loaded ad instance.
                        adInterstitialARepo[adInfo.adKey]?.interstitialAd = interstitialAd1
                        adInterstitialARepo[adInfo.adKey]?.isAdsLoading = false
                        // Invoke the callback indicating successful ad load.
                        callback.onInterstitialAdLoaded()
                    }
                })
        }

    }

    /**
     * Return the instance of the loaded normal interstitial ad.
     *
     * @return The instance of the loaded interstitial ad, or null if not loaded.
     */
    override fun returnNormalInterstitialAd(adKey: Int) = adInterstitialARepo[adKey]

    /**
     * Release the reference to the normal interstitial ad instance.
     * This is typically done when the ad is no longer needed.
     */
    override fun releaseNormalInterstitialAd(adKey: Int) {
        // Set the ad instance to null, releasing the reference.
        adInterstitialARepo[adKey]?.interstitialAd = null
        adInterstitialARepo[adKey]?.isAdsLoading = false
    }

    /**
     * Shows a normal interstitial ad.
     *
     * @param activity The activity where the ad will be displayed.
     * @param interstitialAdLoadCallback The callback to handle interstitialAd results.
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
        if (adInterstitialARepo.containsKey(adInfo.adKey)) {

// Check if a normal interstitial ad is loaded.
            if (adInterstitialARepo[adInfo.adKey]?.interstitialAd != null) {
                // Initialize the full-screen dialog.
                fullScreenDialog = FullScreenDialog(activity)
                // Show the full-screen dialog.
                fullScreenDialog?.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    // Show the interstitial ad.
                    adInterstitialARepo[adInfo.adKey]?.interstitialAd?.show(activity)
                }, 1000)

            } else {
                interstitialAdLoadCallback.onInterstitialAdNotAvailable()
            }

            // Set the full-screen content callback for the interstitial ad.
            adInterstitialARepo[adInfo.adKey]?.interstitialAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    // Callback triggered when the ad is clicked.
                    override fun onAdClicked() {
                        debug("Ad was clicked.")
                        activity.toast("Ad was clicked.")
                        interstitialAdLoadCallback.onInterstitialAdClicked()
                    }

                    // Callback triggered when ad is dismissed.
                    override fun onAdDismissedFullScreenContent() {
                        debug("Ad dismissed fullscreen content.")
                        activity.toast("Ad dismissed fullscreen content.")
                        // Release the ad reference.
                        adInterstitialARepo[adInfo.adKey]?.interstitialAd = null
                        adInterstitialARepo.remove(adInfo.adKey)
                        // Dismiss the full-screen dialog.
                        fullScreenDialog?.dismiss()
                        fullScreenDialog = null
                        interstitialAdLoadCallback.onInterstitialAdDismissed()
                    }

                    // Callback triggered when ad fails to show.
                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        debug("Ad failed to show fullscreen content.")
                        activity.toast("Ad failed to show fullscreen content.")
                        // Dismiss the full-screen dialog.
                        fullScreenDialog?.dismiss()
                        fullScreenDialog = null
                        // Release the ad reference.
                        adInterstitialARepo[adInfo.adKey]?.interstitialAd = null
                        adInterstitialARepo.remove(adInfo.adKey)
                        interstitialAdLoadCallback.onInterstitialAdFailedToShowFullScreenContent(
                            adError
                        )
                    }

                    // Callback triggered when an impression is recorded.
                    override fun onAdImpression() {
                        debug("Ad recorded an impression.")
                        activity.toast("Ad recorded an impression.")
                        interstitialAdLoadCallback.onInterstitialAdImpression()
                    }

                    // Callback triggered when the ad is shown.
                    override fun onAdShowedFullScreenContent() {
                        debug("Ad showed fullscreen content.")
                        activity.toast("Ad showed fullscreen content.")
                        interstitialAdLoadCallback.onInterstitialAdShowed()

                    }
                }
        } else {
            interstitialAdLoadCallback.onInterstitialAdNotAvailable()
            return
        }
    }
}
