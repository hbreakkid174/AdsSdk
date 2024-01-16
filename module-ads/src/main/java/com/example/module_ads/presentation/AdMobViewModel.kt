package com.example.module_ads.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.module_ads.domain.BannerAdRepository
import com.example.module_ads.domain.InterstitialAdRepository
import com.example.module_ads.utils.AdMobAdState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state of AdMob interstitial ads.
 *
 * @property adMobRepository The repository handling AdMob interstitial ad operations.
 * @constructor Injected constructor to provide dependencies.
 */
@HiltViewModel
class AdMobViewModel @Inject constructor(
    private val adMobRepository: InterstitialAdRepository,
    private val bannerAdRepository: BannerAdRepository,
    application: Application
) : AndroidViewModel(application) {

    // LiveData to observe the state of AdMob interstitial ads.
    private val _adMobAdState = MutableLiveData<AdMobAdState>()
    val adMobAdState: LiveData<AdMobAdState> get() = _adMobAdState


    /**
     * Loads a normal interstitial ad with the specified [adUnitId].
     *
     * @param adUnitId The ad unit ID to load the ad.
     */
    fun loadNormalInterstitialAd(adUnitId: String) {
        // Use the repository to load the ad and handle the callback.
        adMobRepository.loadNormalInterstitialAd(
            adUnitId,
            object : InterstitialAdRepository.AdLoadCallback {
                // Callback triggered when the ad is successfully loaded.
                override fun onAdLoaded() {
                    // Update the LiveData with the loaded state.
                    _adMobAdState.value = AdMobAdState.AdLoaded
                }

                // Callback triggered when ad fails to load.
                override fun onAdFailedToLoad(errorCode: Int) {
                    // Update the LiveData with the failed to load state and error code.
                    _adMobAdState.value = AdMobAdState.AdFailedToLoad(errorCode)
                }
            })
    }

    /**
     * Returns the instance of the loaded normal interstitial ad.
     *
     * @return The instance of the loaded interstitial ad, or null if not loaded.
     */
    fun returnNormalInterstitialAd() = adMobRepository.returnNormalInterstitialAd()

    /**
     * Releases the reference to the normal interstitial ad instance.
     * This is typically done when the ad is no longer needed.
     */
    fun releaseNormalInterstitialAd() {
        adMobRepository.releaseNormalInterstitialAd()
    }

    /**
     * Loads a banner ad with the specified [adUnitId].
     *
     * @param adUnitId The ad unit ID to load the ad.
     */

    fun loadBanner(adUnitId: String) {
        bannerAdRepository.loadBannerAd(adUnitId, object : BannerAdRepository.AdLoadCallback {
            // Callback triggered when the ad is successfully loaded.
            override fun onAdLoaded() {
                // Update the LiveData with the loaded state.
                _adMobAdState.value = AdMobAdState.AdLoaded
            }

            // Callback triggered when ad fails to load.
            override fun onAdFailedToLoad(errorCode: Int) {
                // Update the LiveData with the failed to load state and error code.
                _adMobAdState.value = AdMobAdState.AdFailedToLoad(errorCode)
            }
        })
    }

    /**
     * Returns the instance of the loaded banner ad.
     *
     * @return The instance of the loaded banner ad, or null if not loaded.
     */
    fun returnBannerView() = bannerAdRepository.returnBannerAd()

    /**
     * Releases the reference to the banner ad instance.
     * This is typically done when the ad is no longer needed.
     */
    fun destroyBannerAd() {
        bannerAdRepository.destroyBannerAd()
    }

}
