package com.example.module_ads.presentation

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.module_ads.domain.BannerAdRepository
import com.example.module_ads.domain.InterstitialAdRepository
import com.example.module_ads.enums.CollapsibleBannerPosition
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
            object : InterstitialAdRepository.InterstitialAdLoadCallback {
                // Callback triggered when the ad is successfully loaded.
                override fun onInterstitialAdLoaded() {
                    // Update the LiveData with the loaded state.
                    _adMobAdState.value = AdMobAdState.AdLoaded
                }

                // Callback triggered when ad fails to load.
                override fun onInterstitialAdFailedToLoad(errorCode: Int) {
                    // Update the LiveData with the failed to load state and error code.
                    _adMobAdState.value = AdMobAdState.AdFailedToLoad(errorCode)
                }

                //callback triggered when the interstitial ad is not available
                override fun onInterstitialAdNotAvailable() {
                    _adMobAdState.value = AdMobAdState.AdNotAvailable
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

    fun loadBanner(context: Context, adUnitId: String, view: View) {
        bannerAdRepository.loadBannerAd(
            context,
            adUnitId,
            view,
            object : BannerAdRepository.BannerAdLoadCallback {
                // Callback triggered when the ad is successfully loaded.
                override fun onBannerAdLoaded() {
                    // Update the LiveData with the loaded state.
                    _adMobAdState.value = AdMobAdState.AdLoaded
                }

                // Callback triggered when ad fails to load.
                override fun onBannerAdFailedToLoad(errorCode: Int) {
                    // Update the LiveData with the failed to load state and error code.
                    _adMobAdState.value = AdMobAdState.AdFailedToLoad(errorCode)
                }

                //callback triggered when the banner ad is not available
                override fun onBannerAdNotAvailable() {
                    _adMobAdState.value = AdMobAdState.AdNotAvailable
                }
            })
    }

    /**
     * Loads a collapsible banner ad and updates the state accordingly.
     *
     * @param adUnitId The ad unit ID to identify the specific banner ad.
     * @param view The View where the banner ad will be displayed.
     * @param collapsibleBannerPosition The position where the collapsible banner will be displayed (TOP or BOTTOM).
     */
    fun loadCollapsibleBanner(
        context: Context,
        adUnitId: String,
        view: View,
        collapsibleBannerPosition: CollapsibleBannerPosition
    ) {
        // Load the collapsible banner using the bannerAdRepository.
        bannerAdRepository.loadCollapsibleBanner(
            context,
            adUnitId,
            view,
            collapsibleBannerPosition,
            object : BannerAdRepository.BannerAdLoadCallback {
                // Callback triggered when the ad is successfully loaded.
                override fun onBannerAdLoaded() {
                    // Update the LiveData with the loaded state.
                    _adMobAdState.value = AdMobAdState.AdLoaded
                }

                // Callback triggered when ad fails to load.
                override fun onBannerAdFailedToLoad(errorCode: Int) {
                    // Update the LiveData with the failed to load state and error code.
                    _adMobAdState.value = AdMobAdState.AdFailedToLoad(errorCode)
                }

                // Callback triggered when the banner ad is not available.
                override fun onBannerAdNotAvailable() {
                    // Update the LiveData with the ad not available state.
                    _adMobAdState.value = AdMobAdState.AdNotAvailable
                }
            }
        )
    }


    /**
     * Returns the instance of the loaded banner ad.
     *
     * @return The instance of the loaded banner ad, or null if not loaded.
     */
    fun returnBannerAdView() = bannerAdRepository.returnBannerAd()
    fun returnCollapsedBannerAdView() = bannerAdRepository.returnCollapsedBannerAd()







}
