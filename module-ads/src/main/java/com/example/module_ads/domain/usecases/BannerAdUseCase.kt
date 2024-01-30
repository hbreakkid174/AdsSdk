package com.example.module_ads.domain.usecases

import android.content.Context
import android.view.View
import com.example.module_ads.domain.repositories.BannerAdRepository
import com.example.module_ads.enums.CollapsibleBannerPosition
import com.google.android.gms.ads.AdView
import javax.inject.Inject

/**
 * UseCase class responsible for handling operations related to loading and managing banner ads.
 *
 * @param bannerAdRepository The repository providing the implementation details for banner ad operations.
 */
class BannerAdUseCase @Inject constructor(
    private val bannerAdRepository: BannerAdRepository
) {

    /**
     * Loads a standard banner ad with the specified parameters.
     *
     * @param context The context to use for loading the banner ad.
     * @param adUnitId The ad unit ID of the banner ad.
     * @param view The view used to calculate the banner ad size.
     * @param callback Callback to handle banner ad loading events.
     */
    fun loadBannerAd(
        context: Context,
        adUnitId: String,
        view: View,
        callback: BannerAdRepository.BannerAdLoadCallback
    ) {
        bannerAdRepository.loadBannerAd(
            context,
            adUnitId,
            view,
            callback
        )
    }

    /**
     * Loads a collapsible banner ad with the specified parameters.
     *
     * @param context The context to use for loading the banner ad.
     * @param adUnitId The ad unit ID of the banner ad.
     * @param view The view used to calculate the banner ad size.
     * @param collapsibleBannerPosition The position where the collapsible banner will be displayed (TOP or BOTTOM).
     * @param adLoadCallback Callback to handle banner ad loading events.
     */
    fun loadCollapsibleBanner(
        context: Context,
        adUnitId: String,
        view: View,
        collapsibleBannerPosition: CollapsibleBannerPosition,
        adLoadCallback: BannerAdRepository.BannerAdLoadCallback
    ) {
        bannerAdRepository.loadCollapsibleBanner(
            context,
            adUnitId,
            view,
            collapsibleBannerPosition,
            adLoadCallback
        )
    }

    /**
     * Returns the loaded standard banner ad if available.
     *
     * @return The loaded AdView representing the standard banner ad, or null if not loaded.
     */
    fun returnBannerAd(): AdView? {
        return bannerAdRepository.returnBannerAd()
    }

    /**
     * Returns the loaded collapsible banner ad if available.
     *
     * @return The loaded AdView representing the collapsible banner ad, or null if not loaded.
     */
    fun returnCollapsedBannerAd(): AdView? {
        return bannerAdRepository.returnCollapsedBannerAd()
    }

    /**
     * Resumes the standard banner ad.
     */
    fun resumeBannerAd() {
        bannerAdRepository.resumeBannerAd()
    }

    /**
     * Pauses the standard banner ad.
     */
    fun pauseBannerAd() {
        bannerAdRepository.pauseBannerAd()
    }

    /**
     * Destroys the standard banner ad.
     */
    fun destroyBannerAd() {
        bannerAdRepository.destroyBannerAd()
    }

    /**
     * Resumes the collapsible banner ad.
     */
    fun resumeCollapsibleBanner() {
        bannerAdRepository.resumeCollapsibleBanner()
    }

    /**
     * Pauses the collapsible banner ad.
     */
    fun pauseCollapsibleBanner() {
        bannerAdRepository.pauseCollapsibleBanner()
    }

    /**
     * Destroys the collapsible banner ad.
     */
    fun destroyCollapsibleBanner() {
        bannerAdRepository.destroyCollapsibleBanner()
    }
}