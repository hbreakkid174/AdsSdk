package com.example.module_ads.domain.usecases

import android.app.Activity
import com.example.module_ads.data.model.InterstitialAdInfo
import com.example.module_ads.domain.repositories.InterstitialAdRepository
import com.example.module_ads.domain.repositories.InterstitialAdRepository.InterstitialAdLoadCallback
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
        adInfo: InterstitialAdInfo,
        isPurchased: Boolean = false,
        callback: InterstitialAdLoadCallback
    ) {
        interstitialAdRepository.loadNormalInterstitialAd(adInfo,isPurchased, callback)
    }

    /**
     * Returns the instance of the loaded normal interstitial ad.
     *
     * @return The instance of the loaded interstitial ad, or null if not loaded.
     */
    fun returnNormalInterstitialAd(adKey: Int) = interstitialAdRepository.returnNormalInterstitialAd(adKey)

    /**
     * Releases the reference to the normal interstitial ad instance.
     * This is typically done when the ad is no longer needed.
     */
    fun releaseNormalInterstitialAd(adKey: Int) = interstitialAdRepository.releaseNormalInterstitialAd(adKey)

    /**
     * Shows a normal interstitial ad.
     *
     * @param activity The activity where the ad will be displayed.
     * @param callback The callback to handle interstitial ad results.
     */
    fun showNormalInterstitialAd(
        adInfo: InterstitialAdInfo,
        isPurchased: Boolean = false,
        activity: Activity,
        callback: InterstitialAdRepository.InterstitialAdLoadCallback
    ) {
        interstitialAdRepository.showNormalInterstitialAd(adInfo,isPurchased,activity, callback)
    }
}
