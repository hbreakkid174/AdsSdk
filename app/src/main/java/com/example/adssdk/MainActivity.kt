package com.example.adssdk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.adssdk.databinding.ActivityMainBinding
import com.example.module_ads.domain.repositories.BannerAdRepository
import com.example.module_ads.enums.CollapsibleBannerPosition
import com.example.module_ads.presentation.AdMobViewModel
import com.example.module_ads.utils.AdsConsentManager
import com.example.module_ads.domain.repositories.InterstitialAdRepository
import com.example.module_ads.domain.repositories.NativeAdRepository
import com.example.module_ads.utils.initializeAdmobSdk
import com.example.module_ads.views.debug
import com.example.module_ads.views.displayBannerAd
import com.google.android.gms.ads.LoadAdError
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
                    adMobViewModel.loadNormalInterstitialAd(BuildConfig.ad_interstitial,
                        object : InterstitialAdRepository.InterstitialAdLoadCallback {
                            override fun onInterstitialAdNotAvailable() {

                            }

                        })
                }
            }
            showAdButton.setOnClickListener {
                adMobViewModel.showNormalInterstitialAd(this@MainActivity,
                    object : InterstitialAdRepository.InterstitialAdLoadCallback {
                        override fun onInterstitialAdNotAvailable() {

                        }

                        override fun onInterstitialAdDismissed() {
                            debug("ad dismissed from state")
                        }

                    })

            }
            nextButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, TestScreenActivitiy::class.java))

            }
            nativeAdShow.setOnClickListener {
                startActivity(Intent(this@MainActivity, NativeAdTestActivity::class.java))
            }
            loadCollapsibleBanner()
        }

//        preLoadNativeAd()

    }

    private fun preLoadNativeAd() {
        if (adsConsentManager?.canRequestAds == true) {
            adMobViewModel.loadNativeAd(
                this,
                BuildConfig.ad_native,
                object : NativeAdRepository.NativeAdLoadCallback {
                    override fun onNativeAdLoaded() {}

                    override fun onNativeAdFailedToLoad(loadAdError: LoadAdError) {}

                    override fun onNativeAdNotAvailable() {}

                })
        }
    }

    private fun loadCollapsibleBanner() {

        binding?.apply {
            if (adsConsentManager?.canRequestAds == true) {
                adMobViewModel.loadCollapsibleBanner(
                    this@MainActivity,
                    BuildConfig.ad_banner_collapsible, adViewContainer,
                    CollapsibleBannerPosition.BOTTOM,
                    object : BannerAdRepository.BannerAdLoadCallback {
                        override fun onBannerAdLoaded() {
                            displayBannerAd(
                                adViewContainer,
                                adMobViewModel.returnCollapsedBannerAdView()
                            )
                        }

                        override fun onBannerAdFailedToLoad(errorCode: Int) {
                            adViewContainer.visibility = View.GONE

                        }

                        override fun onBannerAdNotAvailable() {
                            adViewContainer.visibility = View.GONE

                        }

                    }
                )
            }


        }

    }

    override fun onResume() {
        super.onResume()
        adMobViewModel.resumeCollapsibleBanner()
    }

    override fun onPause() {
        super.onPause()
        adMobViewModel.pauseCollapsibleBanner()
    }

    override fun onDestroy() {
        super.onDestroy()
        adMobViewModel.destroyCollapsibleBanner()
    }

    private fun initSdk() {
        initializeAdmobSdk {
            Log.d(TAG, "onCreate: initializeAdmobSdk")
        }
    }


}
