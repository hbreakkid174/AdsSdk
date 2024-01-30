package com.example.module_ads.presentation

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.AndroidViewModel
import com.example.module_ads.domain.repositories.BannerAdRepository
import com.example.module_ads.domain.repositories.InterstitialAdRepository
import com.example.module_ads.domain.repositories.NativeAdRepository
import com.example.module_ads.domain.usecases.BannerAdUseCase
import com.example.module_ads.domain.usecases.InterstitialAdUseCase
import com.example.module_ads.domain.usecases.NativeAdUseCase
import com.example.module_ads.enums.CollapsibleBannerPosition
import com.example.module_ads.enums.NativeAdType
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


    /**
     * Loads a normal interstitial ad with the specified [adUnitId].
     *
     * @param adUnitId The ad unit ID to load the ad.
     */
    fun loadNormalInterstitialAd(
        adUnitId: String,
        interstitialAdLoadCallback: InterstitialAdRepository.InterstitialAdLoadCallback
    ) {
        // Use the repository to load the ad and handle the callback.
        interstitialAdUseCase.loadNormalInterstitialAd(adUnitId, interstitialAdLoadCallback)
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

    fun loadBanner(
        context: Context,
        adUnitId: String,
        view: View,
        callback: BannerAdRepository.BannerAdLoadCallback
    ) {
        bannerAdUseCase.loadBannerAd(
            context,
            adUnitId,
            view, callback
        )
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
        collapsibleBannerPosition: CollapsibleBannerPosition,
        callback: BannerAdRepository.BannerAdLoadCallback
    ) {
        // Load the collapsible banner using the bannerAdRepository.
        bannerAdUseCase.loadCollapsibleBanner(
            context,
            adUnitId,
            view,
            collapsibleBannerPosition, callback
        )
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

    fun showNormalInterstitialAd(
        activity: Activity,
        interstitialAdLoadCallback: InterstitialAdRepository.InterstitialAdLoadCallback
    ) {
        interstitialAdUseCase.showNormalInterstitialAd(
            activity, interstitialAdLoadCallback
        )
    }

    fun loadNativeAd(
        activity: Activity, adUnitId: String,
        nativeAdLoadCallback: NativeAdRepository.NativeAdLoadCallback
    ) {
        nativeAdUseCase.loadNativeAd(
            activity,
            adUnitId,
            nativeAdLoadCallback
        )
    }

    fun destroyNativeAd() {
        nativeAdUseCase.destroyNativeAd()
    }

    fun populateNativeAdView(
        activity: Activity,
        nativeAdContainer: FrameLayout,
        nativeAdType: NativeAdType
    ) {
        nativeAdUseCase.populateNativeAdView(activity, nativeAdContainer, nativeAdType)
    }


    fun resumeBannerAd(){
        bannerAdUseCase.resumeBannerAd()
    }

    fun pauseBannerAd(){
        bannerAdUseCase.pauseBannerAd()
    }

    fun destroyBannerAd(){
        bannerAdUseCase.destroyBannerAd()
    }
    fun resumeCollapsibleBanner(){
        bannerAdUseCase.resumeCollapsibleBanner()
    }

    fun pauseCollapsibleBanner(){
        bannerAdUseCase.pauseCollapsibleBanner()
    }

    fun destroyCollapsibleBanner(){
        bannerAdUseCase.destroyCollapsibleBanner()
    }
}
