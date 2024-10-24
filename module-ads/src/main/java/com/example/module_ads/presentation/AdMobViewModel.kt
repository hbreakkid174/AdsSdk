package com.example.module_ads.presentation

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.AndroidViewModel
import com.example.module_ads.data.model.InterstitialAdInfo
import com.example.module_ads.domain.repositories.BannerAdRepository
import com.example.module_ads.domain.repositories.InterstitialAdRepository.InterstitialAdLoadCallback
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
     * Initiates the loading of a normal interstitial ad with the specified [adInfo].
     *
     * @param adInfo Contains information such as ad unit ID and configuration details for the ad.
     * @param isPurchased A flag indicating whether the user has made a purchase that disables ads.
     *                    If true, the ad won't be loaded.
     * @param callback The [InterstitialAdLoadCallback] to handle loading events (e.g., success or failure).
     */
    fun loadNormalInterstitialAd(
        adInfo: InterstitialAdInfo,
        isPurchased: Boolean = false,
        callback: InterstitialAdLoadCallback
    ) {
        // Use the repository to load the ad and handle the callback.
        interstitialAdUseCase.loadNormalInterstitialAd(adInfo, isPurchased,callback)
    }

    /**
     * Retrieves the currently loaded normal interstitial ad instance using the provided [adKey].
     *
     * @param adKey The unique identifier for the ad.
     * @return The instance of [InterstitialAdInfo] that represents the loaded interstitial ad, or null if not loaded.
     */
    fun returnNormalInterstitialAd(adKey: Int) = interstitialAdUseCase.returnNormalInterstitialAd(adKey)

    /**
     * Releases the reference to the interstitial ad for the specified [adKey].
     * This should be called when the ad is no longer needed, to free up resources.
     *
     * @param adKey The unique identifier for the ad to be released.
     */
    fun releaseNormalInterstitialAd(adKey: Int) {
        interstitialAdUseCase.releaseNormalInterstitialAd(adKey)
    }
    /**
     * Displays a normal interstitial ad within the specified [activity].
     *
     * @param adInfo The [InterstitialAdInfo] containing details of the ad to be shown.
     * @param isPurchased A flag indicating whether the user has made a purchase that disables ads.
     *                    If true, the ad won't be shown.
     * @param activity The activity where the interstitial ad will be displayed.
     * @param interstitialAdLoadCallback The callback to handle events related to displaying the ad (e.g., impressions, dismissals).
     */
    fun showNormalInterstitialAd(
        adInfo: InterstitialAdInfo,
        isPurchased: Boolean = false,
        activity: Activity,
        interstitialAdLoadCallback: InterstitialAdLoadCallback
    ) {
        interstitialAdUseCase.showNormalInterstitialAd(
            adInfo,isPurchased,
            activity, interstitialAdLoadCallback
        )
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