package com.example.module_ads.domain.usecases

import android.app.Activity
import com.example.module_ads.domain.repositories.InterstitialAdRepository
import javax.inject.Inject

/**
 * UseCase class responsible for handling operations related to loading and managing interstitial ads.
 *
 * @param interstitialAdRepository The repository providing the implementation details for interstitial ad operations.
 */
class InterstitialAdUseCase @Inject constructor(
    private val interstitialAdRepository: InterstitialAdRepository
) {

    /**
     * Loads a normal interstitial ad with the specified [adUnitId].
     *
     * @param adUnitId The ad unit ID to load the ad.
     * @param callback The callback to handle ad loading results.
     */
    fun loadNormalInterstitialAd(
        adUnitId: String,
        callback: InterstitialAdRepository.InterstitialAdLoadCallback
    ) {
        interstitialAdRepository.loadNormalInterstitialAd(adUnitId, callback)
    }

    /**
     * Returns the instance of the loaded normal interstitial ad.
     *
     * @return The instance of the loaded interstitial ad, or null if not loaded.
     */
    fun returnNormalInterstitialAd() = interstitialAdRepository.returnNormalInterstitialAd()

    /**
     * Releases the reference to the normal interstitial ad instance.
     * This is typically done when the ad is no longer needed.
     */
    fun releaseNormalInterstitialAd() = interstitialAdRepository.releaseNormalInterstitialAd()

    /**
     * Shows a normal interstitial ad.
     *
     * @param activity The activity where the ad will be displayed.
     * @param callback The callback to handle interstitial ad results.
     */
    fun showNormalInterstitialAd(
        activity: Activity,
        callback: InterstitialAdRepository.InterstitialAdLoadCallback
    ) {
        interstitialAdRepository.showNormalInterstitialAd(activity, callback)
    }
}
