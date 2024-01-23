package com.example.module_ads.presentation

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.module_ads.domain.repositories.BannerAdRepository
import com.example.module_ads.domain.repositories.InterstitialAdRepository
import com.example.module_ads.enums.CollapsibleBannerPosition
import com.example.module_ads.adstates.BannerAdState
import com.example.module_ads.adstates.CollapsibleBannerAdState
import com.example.module_ads.adstates.InterstitialAdState
import com.example.module_ads.adstates.NativeAdState
import com.example.module_ads.domain.repositories.NativeAdRepository
import com.example.module_ads.domain.usecases.BannerAdUseCase
import com.example.module_ads.domain.usecases.InterstitialAdUseCase
import com.example.module_ads.domain.usecases.NativeAdUseCase
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state of AdMob interstitial ads.
 *
 * @property interstitialAdUseCase The use case handling AdMob interstitial ad operations.
 * @property bannerAdUseCase The use case handling AdMob banner ad operations.
 * @property nativeAdUseCase The use case handling AdMob native ad operations.
 * @constructor Injected constructor to provide dependencies.
 */
@HiltViewModel
class AdMobViewModel @Inject constructor(
    private val interstitialAdUseCase: InterstitialAdUseCase,
    private val bannerAdUseCase: BannerAdUseCase,
    private val nativeAdUseCase: NativeAdUseCase,
    application: Application
) : AndroidViewModel(application) {


    // LiveData to observe the state of AdMob interstitial ads.
    private val _interstitialAdState = MutableLiveData<InterstitialAdState>()
    val interstitialAdState: LiveData<InterstitialAdState> get() = _interstitialAdState

    // LiveData to observe the state of AdMob banner ads.
    private val _bannerAdState = MutableLiveData<BannerAdState>()
    val bannerAdState: LiveData<BannerAdState> get() = _bannerAdState

    // LiveData to observe the state of AdMob collapsible banner ads.
    private val _collapsibleBannerAdState = MutableLiveData<CollapsibleBannerAdState>()
    val collapsibleBannerAdState: LiveData<CollapsibleBannerAdState> get() = _collapsibleBannerAdState

    // LiveData to observe the state of AdMob native ads.
    private val _nativeAdState = MutableLiveData<NativeAdState>()
    val nativeAdState: LiveData<NativeAdState> get() = _nativeAdState

    /**
     * Loads a normal interstitial ad with the specified [adUnitId].
     *
     * @param adUnitId The ad unit ID to load the ad.
     */
    fun loadNormalInterstitialAd(adUnitId: String) {
        // Use the repository to load the ad and handle the callback.
        interstitialAdUseCase.loadNormalInterstitialAd(adUnitId,
            object : InterstitialAdRepository.InterstitialAdLoadCallback {
                // Callback triggered when the ad is successfully loaded.
                override fun onInterstitialAdLoaded() {
                    // Update the LiveData with the loaded state.
                    _interstitialAdState.value = InterstitialAdState.AdLoaded
                }

                // Callback triggered when ad fails to load.
                override fun onInterstitialAdFailedToLoad(errorCode: Int) {
                    // Update the LiveData with the failed to load state and error code.
                    _interstitialAdState.value = InterstitialAdState.AdFailedToLoad(errorCode)
                }

                //callback triggered when the interstitial ad is not available
                override fun onInterstitialAdNotAvailable() {
                    _interstitialAdState.value = InterstitialAdState.AdNotAvailable
                }
            })
    }

    /**
     * Returns the instance of the loaded normal interstitial ad.
     *
     * @return The instance of the loaded interstitial ad, or null if not loaded.
     */
    fun returnNormalInterstitialAd() = interstitialAdUseCase.returnNormalInterstitialAd()

    /**
     * Releases the reference to the normal interstitial ad instance.
     * This is typically done when the ad is no longer needed.
     */
    fun releaseNormalInterstitialAd() {
        interstitialAdUseCase.releaseNormalInterstitialAd()
    }

    /**
     * Loads a banner ad with the specified [adUnitId].
     *
     * @param adUnitId The ad unit ID to load the ad.
     */

    fun loadBanner(context: Context, adUnitId: String, view: View) {
        bannerAdUseCase.loadBannerAd(context,
            adUnitId,
            view,
            object : BannerAdRepository.BannerAdLoadCallback {
                // Callback triggered when the ad is successfully loaded.
                override fun onBannerAdLoaded() {
                    // Update the LiveData with the loaded state.
                    _bannerAdState.value = BannerAdState.AdLoaded
                }

                // Callback triggered when ad fails to load.
                override fun onBannerAdFailedToLoad(errorCode: Int) {
                    // Update the LiveData with the failed to load state and error code.
                    _bannerAdState.value = BannerAdState.AdFailedToLoad(errorCode)
                }

                //callback triggered when the banner ad is not available
                override fun onBannerAdNotAvailable() {
                    _bannerAdState.value = BannerAdState.AdNotAvailable
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
        bannerAdUseCase.loadCollapsibleBanner(context,
            adUnitId,
            view,
            collapsibleBannerPosition,
            object : BannerAdRepository.BannerAdLoadCallback {
                // Callback triggered when the ad is successfully loaded.
                override fun onBannerAdLoaded() {
                    // Update the LiveData with the loaded state.
                    _collapsibleBannerAdState.value = CollapsibleBannerAdState.AdLoaded
                }

                // Callback triggered when ad fails to load.
                override fun onBannerAdFailedToLoad(errorCode: Int) {
                    // Update the LiveData with the failed to load state and error code.
                    _collapsibleBannerAdState.value =
                        CollapsibleBannerAdState.AdFailedToLoad(errorCode)
                }

                // Callback triggered when the banner ad is not available.
                override fun onBannerAdNotAvailable() {
                    // Update the LiveData with the ad not available state.
                    _collapsibleBannerAdState.value = CollapsibleBannerAdState.AdNotAvailable
                }
            })
    }


    /**
     * Returns the instance of the loaded banner ad.
     *
     * @return The instance of the loaded banner ad, or null if not loaded.
     */
    fun returnBannerAdView() = bannerAdUseCase.returnBannerAd()

    /**
     * Returns the instance of the loaded collapsible banner ad.
     *
     * @return The instance of the loaded collapsible banner ad, or null if not loaded.
     */
    fun returnCollapsedBannerAdView() = bannerAdUseCase.returnCollapsedBannerAd()

    fun showNormalInterstitialAd(activity: Activity) {
        interstitialAdUseCase.showNormalInterstitialAd(
            activity,
            object : InterstitialAdRepository.InterstitialAdLoadCallback {
                override fun onInterstitialAdNotAvailable() {
                    _interstitialAdState.value = InterstitialAdState.AdNotAvailable
                }

                override fun onInterstitialAdShowed() {
                    _interstitialAdState.value = InterstitialAdState.AdShowed
                }

                override fun onInterstitialAdClicked() {
                    _interstitialAdState.value = InterstitialAdState.AdClicked
                }

                override fun onInterstitialAdDismissed() {
                    _interstitialAdState.value = InterstitialAdState.AdDismissed
                }

                override fun onInterstitialAdFailedToShowFullScreenContent(adError: AdError) {
                    _interstitialAdState.value =
                        InterstitialAdState.AdFailedToShowFullScreenContent(adError)
                }

                override fun onInterstitialAdImpression() {
                    _interstitialAdState.value = InterstitialAdState.AdImpression
                }

            })
    }

    fun loadNativeAd(activity: Activity, adUnitId: String) {
        nativeAdUseCase.loadNativeAd(
            activity,
            adUnitId,
            object : NativeAdRepository.NativeAdLoadCallback {
                override fun onNativeAdLoaded() {
                    _nativeAdState.value = NativeAdState.AdLoaded
                }

                override fun onNativeAdFailedToLoad(loadAdError: LoadAdError) {
                    _nativeAdState.value = NativeAdState.AdFailedToLoad(loadAdError)
                }

                override fun onNativeAdImpression() {
                    _nativeAdState.value = NativeAdState.AdImpression
                }

                override fun onNativeAdClicked() {
                    _nativeAdState.value = NativeAdState.AdClicked
                }

                override fun onNativeAdNotAvailable() {
                    _nativeAdState.value = NativeAdState.AdNotAvailable
                }
            })
    }

}
