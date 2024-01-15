package com.example.module_ads.data

import android.content.Context
import com.example.module_ads.domain.BannerAdRepository
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import javax.inject.Inject

/**
 * Implementation of [BannerAdRepository] responsible for loading and returning banner ads.
 */
class BannerAdRepositoryImpl @Inject constructor(private val context: Context) : BannerAdRepository {

    // Declare the AdView variable
    private lateinit var adView: AdView

    /**
     * Loads a banner ad with the specified ad unit ID.
     *
     * @param adUnitId The ad unit ID of the banner ad.
     * @param adLoadCallback Callback to handle ad loading events.
     */
    override fun loadBannerAd(adUnitId: String, adLoadCallback: BannerAdRepository.AdLoadCallback) {
        // Initialize the AdView
        adView = AdView(context)
        adView.adUnitId = adUnitId
        adView.setAdSize(AdSize.BANNER)

        // Create an ad request.
        val adRequest = AdRequest.Builder().build()

        // Start loading the ad in the background.
        adView.loadAd(adRequest)

        // Set up an ad listener to handle loading events
        adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                adLoadCallback.onAdFailedToLoad(adError.code)
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                adLoadCallback.onAdLoaded()
            }
        }
    }

    /**
     * Returns the loaded banner ad if available.
     *
     * @return The loaded AdView representing the banner ad, or null if not loaded.
     */
    override fun returnBannerAd(): AdView? = adView
}
