package com.example.module_ads.domain.usecases

import android.app.Activity
import com.example.module_ads.data.model.InterstitialAdInfo
import com.example.module_ads.domain.repositories.InterstitialAdRepository
import com.example.module_ads.domain.repositories.InterstitialAdRepository.InterstitialAdLoadCallback
import javax.inject.Inject

/**
 * Use case class responsible for handling operations related to interstitial ad loading, management, and display.
 *
 * @param interstitialAdRepository The repository responsible for managing the interstitial ad operations.
 */
class InterstitialAdUseCase @Inject constructor(
    private val interstitialAdRepository: InterstitialAdRepository
) {

    /**
     * Initiates the loading of a normal interstitial ad.
     *
     * @param adInfo The [InterstitialAdInfo] containing ad configuration details (e.g., ad unit ID).
     * @param isPurchased Flag indicating whether the user has purchased a service or subscription
     *        that disables ads. If true, the ad will not be loaded.
     * @param callback The [InterstitialAdLoadCallback] to handle the result of the ad loading process.
     */
    fun loadNormalInterstitialAd(
        adInfo: InterstitialAdInfo,
        isPurchased: Boolean = false,
        callback: InterstitialAdLoadCallback
    ) {
        interstitialAdRepository.loadNormalInterstitialAd(adInfo, isPurchased, callback)
    }

    /**
     * Retrieves the instance of the loaded interstitial ad based on the provided [adKey].
     *
     * @param adKey The unique identifier for the ad.
     * @return The [InterstitialAdInfo] containing the loaded interstitial ad, or null if not loaded.
     */
    fun returnNormalInterstitialAd(adKey: Int) =
        interstitialAdRepository.returnNormalInterstitialAd(adKey)

    /**
     * Releases the reference to the interstitial ad instance associated with the given [adKey].
     * This should be called when the ad is no longer needed to free up memory and resources.
     *
     * @param adKey The unique identifier for the ad to be released.
     */
    fun releaseNormalInterstitialAd(adKey: Int) =
        interstitialAdRepository.releaseNormalInterstitialAd(adKey)

    /**
     * Displays a normal interstitial ad in the specified [activity].
     *
     * @param adInfo The [InterstitialAdInfo] containing details of the ad to be shown.
     * @param isPurchased Flag indicating whether the user has purchased a service or subscription
     *        that removes ads. If true, the ad will not be shown.
     * @param activity The [Activity] in which the interstitial ad will be displayed.
     * @param callback The [InterstitialAdRepository.InterstitialAdLoadCallback] to handle events during ad display.
     */
    fun showNormalInterstitialAd(
        adInfo: InterstitialAdInfo,
        isPurchased: Boolean = false,
        activity: Activity,
        callback: InterstitialAdRepository.InterstitialAdLoadCallback
    ) {
        interstitialAdRepository.showNormalInterstitialAd(adInfo, isPurchased, activity, callback)
    }
}
