package com.example.adssdk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.adssdk.databinding.ActivityMainBinding
import com.example.module_ads.enums.CollapsibleBannerPosition
import com.example.module_ads.presentation.AdMobViewModel
import com.example.module_ads.utils.AdsConsentManager
import com.example.module_ads.adstates.CollapsibleBannerAdState
import com.example.module_ads.adstates.InterstitialAdState
import com.example.module_ads.utils.initializeAdmobSdk
import com.example.module_ads.views.debug
import com.example.module_ads.views.displayBannerAd
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val adMobViewModel: AdMobViewModel by viewModels()
    private var binding: ActivityMainBinding? = null


    companion object {
        private var TAG = "MainActivity"
    }


    private var adsConsentManager: AdsConsentManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
//        OpenAdHelper.disableResumeAd()
        adsConsentManager = AdsConsentManager(this) {
            if (adsConsentManager?.canRequestAds == true) {
                initSdk()
            }
        }

        binding?.apply {
            loadAdButton.setOnClickListener {
                if (adsConsentManager?.canRequestAds == true) {
                    adMobViewModel.loadNormalInterstitialAd(BuildConfig.ad_interstitial)
                }
            }
            showAdButton.setOnClickListener {
                adMobViewModel.showNormalInterstitialAd(this@MainActivity)
                adMobViewModel.interstitialAdState.observe(this@MainActivity) {
                    when (it) {
                        is InterstitialAdState.AdDismissed -> debug("ad dismissed from state")
                        else -> {}
                    }
                }
            }
            nextButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, TestScreenActivitiy::class.java))

            }
            nativeAdShow.setOnClickListener {
                startActivity(Intent(this@MainActivity, NativeAdTestActivity::class.java))
            }
            loadCollapsibleBanner()
        }

        preLoadNativeAd()

    }

    private fun preLoadNativeAd() {
        if (adsConsentManager?.canRequestAds == true) {
            adMobViewModel.loadNativeAd(this, BuildConfig.ad_native)
        }
    }

    private fun loadCollapsibleBanner() {

        binding?.apply {
            if (adsConsentManager?.canRequestAds == true) {
                adMobViewModel.loadCollapsibleBanner(
                    this@MainActivity,
                    BuildConfig.ad_banner_collapsible, adViewContainer,
                    CollapsibleBannerPosition.BOTTOM
                )
                adMobViewModel.collapsibleBannerAdState.observe(this@MainActivity) {
                    when (it) {
                        is CollapsibleBannerAdState.AdLoaded -> {
                            displayBannerAd(
                                adViewContainer,
                                adMobViewModel.returnCollapsedBannerAdView()
                            )
                        }

                        is CollapsibleBannerAdState.AdFailedToLoad -> {
                            adViewContainer.visibility = View.GONE
                        }

                        is CollapsibleBannerAdState.AdNotAvailable -> {
                            adViewContainer.visibility = View.GONE
                        }
                    }
                }
            }


        }

    }


    private fun initSdk() {
        initializeAdmobSdk {
            Log.d(TAG, "onCreate: initializeAdmobSdk")
        }
    }


}
