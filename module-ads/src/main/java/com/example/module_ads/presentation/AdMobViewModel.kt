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
 * ViewModel responsible for managing the state of AdMob interstitial ads, banner ads, and native ads.
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
     * @param interstitialAdLoadCallback Callback to handle interstitial ad loading events.
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
     * @param context The context to use for loading the banner ad.
     * @param view The view used to calculate the banner ad size.
     * @param callback Callback to handle banner ad loading events.
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
            view,
            callback
        )
    }

    /**
     * Loads a collapsible banner ad and updates the state accordingly.
     *
     * @param adUnitId The ad unit ID to identify the specific banner ad.
     * @param context The context to use for loading the banner ad.
     * @param view The View where the banner ad will be displayed.
     * @param collapsibleBannerPosition The position where the collapsible banner will be displayed (TOP or BOTTOM).
     * @param callback Callback to handle banner ad loading events.
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
            collapsibleBannerPosition,
            callback
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

    /**
     * Shows a normal interstitial ad.
     *
     * @param activity The activity where the interstitial ad will be displayed.
     * @param interstitialAdLoadCallback Callback to handle interstitial ad loading events.
     */
    fun showNormalInterstitialAd(
        activity: Activity,
        interstitialAdLoadCallback: InterstitialAdRepository.InterstitialAdLoadCallback
    ) {
        interstitialAdUseCase.showNormalInterstitialAd(
            activity, interstitialAdLoadCallback
        )
    }

    /**
     * Loads a native ad with the specified ad unit ID.
     *
     * @param activity The activity where the native ad will be loaded.
     * @param adUnitId The ad unit ID of the native ad.
     * @param nativeAdLoadCallback Callback to handle native ad loading events.
     */
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

    /**
     * Destroys the currently loaded native ad.
     */
    fun destroyNativeAd() {
        nativeAdUseCase.destroyNativeAd()
    }

    /**
     * Populates a native ad view within a specified container.
     *
     * @param activity The activity where the native ad view will be populated.
     * @param nativeAdContainer The container for displaying the native ad view.
     * @param nativeAdType The type of native ad to be displayed.
     */
    fun populateNativeAdView(
        activity: Activity,
        nativeAdContainer: FrameLayout,
        nativeAdType: NativeAdType
    ) {
        nativeAdUseCase.populateNativeAdView(activity, nativeAdContainer, nativeAdType)
    }

    /**
     * Resumes the banner ad.
     */
    fun resumeBannerAd() {
        bannerAdUseCase.resumeBannerAd()
    }

    /**
     * Pauses the banner ad.
     */
    fun pauseBannerAd() {
        bannerAdUseCase.pauseBannerAd()
    }

    /**
     * Destroys the banner ad.
     */
    fun destroyBannerAd() {
        bannerAdUseCase.destroyBannerAd()
    }

    /**
     * Resumes the collapsible banner ad.
     */
    fun resumeCollapsibleBanner() {
        bannerAdUseCase.resumeCollapsibleBanner()
    }

    /**
     * Pauses the collapsible banner ad.
     */
    fun pauseCollapsibleBanner() {
        bannerAdUseCase.pauseCollapsibleBanner()
    }

    /**
     * Destroys the collapsible banner ad.
     */
    fun destroyCollapsibleBanner() {
        bannerAdUseCase.destroyCollapsibleBanner()
    }
}