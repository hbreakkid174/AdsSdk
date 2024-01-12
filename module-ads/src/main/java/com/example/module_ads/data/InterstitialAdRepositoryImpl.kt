package com.example.module_ads.data

import android.content.Context
import android.util.Log
import com.example.module_ads.domain.InterstitialAdRepository
import com.google.android.gms.ads.AdRequest
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

    // The InterstitialAd instance for normal interstitial ads.
    private var normalInterstitialAd: InterstitialAd? = null

    /**
     * Load a normal interstitial ad with the specified [adUnitId].
     *
     * @param adUnitId The ad unit ID to load the ad.
     * @param callback The callback to handle ad loading results.
     */
    override fun loadNormalInterstitialAd(adUnitId: String, callback: InterstitialAdRepository.AdLoadCallback) {
        // Check if the ad is not already loaded.
        if (normalInterstitialAd == null) {
            // Build an AdRequest to load the interstitial ad.
            val adRequest = AdRequest.Builder().build()

            // Load the interstitial ad.
            InterstitialAd.load(
                context,
                adUnitId,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    // Callback triggered when ad fails to load.
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d("TAG", adError.toString())
                        // Set the ad instance to null.
                        normalInterstitialAd = null
                        // Invoke the callback with the error code.
                        callback.onAdFailedToLoad(adError.code)
                    }

                    // Callback triggered when ad is successfully loaded.
                    override fun onAdLoaded(interstitialAd1: InterstitialAd) {
                        Log.d("TAG", "Ad was loaded.")
                        // Set the loaded ad instance.
                        normalInterstitialAd = interstitialAd1
                        // Invoke the callback indicating successful ad load.
                        callback.onAdLoaded()
                    }
                })
        }
    }

    /**
     * Return the instance of the loaded normal interstitial ad.
     *
     * @return The instance of the loaded interstitial ad, or null if not loaded.
     */
    override fun returnNormalInterstitialAd() = normalInterstitialAd

    /**
     * Release the reference to the normal interstitial ad instance.
     * This is typically done when the ad is no longer needed.
     */
    override fun releaseNormalInterstitialAd() {
        // Set the ad instance to null, releasing the reference.
        normalInterstitialAd = null
    }
}
