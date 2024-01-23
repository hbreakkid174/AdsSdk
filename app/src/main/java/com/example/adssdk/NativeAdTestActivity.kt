package com.example.adssdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.adssdk.databinding.ActivityNativeAdTestBinding
import com.example.module_ads.adstates.NativeAdState
import com.example.module_ads.presentation.AdMobViewModel
import com.example.module_ads.utils.AdsConsentManager
import com.example.module_ads.views.debug
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NativeAdTestActivity : AppCompatActivity() {
    private var binding: ActivityNativeAdTestBinding? = null
    private val adMobViewModel: AdMobViewModel by viewModels()
    private var adsConsentManager: AdsConsentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeAdTestBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        adsConsentManager = AdsConsentManager(this) {}
        if (adsConsentManager?.canRequestAds == true) {
            adMobViewModel.loadNativeAd(this, BuildConfig.ad_native)
            adMobViewModel.nativeAdState.observe(this) {
                when (it) {
                    is NativeAdState.AdLoaded -> {

                        debug("NativeAdState->load")
                    }
                    is NativeAdState.AdFailedToLoad -> {
                        debug("NativeAdState->failed: ${it.loadAdError}")

                    }
                    is NativeAdState.AdNotAvailable -> {
                        debug("NativeAdState->not available")
                    }
                    is NativeAdState.AdClicked -> {
                        debug("NativeAdState->clicked")
                    }
                    is NativeAdState.AdImpression -> {
                        debug("NativeAdState->impression")
                    }
                }
            }
        }

    }
}