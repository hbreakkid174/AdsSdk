package com.example.adssdk

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.adssdk.databinding.ActivityTestScreenActivitiyBinding
import com.example.module_ads.interstitial.InterstitialAdHelper
import com.example.module_ads.presentation.AdMobViewModel
import com.example.module_ads.utils.AdMobAdState
import com.example.module_ads.utils.AdsConsentManager
import com.example.module_ads.views.displayBannerAd
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TestScreenActivitiy : AppCompatActivity() {
    private var binding: ActivityTestScreenActivitiyBinding? = null
    private val adMobViewModel: AdMobViewModel by viewModels()

    @Inject
    lateinit var interstitialAdHelper: InterstitialAdHelper
    private var adsConsentManager: AdsConsentManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestScreenActivitiyBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        adsConsentManager = AdsConsentManager(this) {}
//        OpenAdHelper.enableResumeAd()
        binding?.apply {
            showAdButton.setOnClickListener {
                interstitialAdHelper.showNormalInterstitialAd(
                    this@TestScreenActivitiy,
                    adMobViewModel
                )
            }
            if (adsConsentManager?.canRequestAds == true) {
                adMobViewModel.loadBanner(
                    this@TestScreenActivitiy,
                    BuildConfig.ad_banner, adViewContainer
                )
                adMobViewModel.adMobAdState.observe(this@TestScreenActivitiy) {
                    when (it) {
                        is AdMobAdState.AdLoaded -> {
                            displayBannerAd(adViewContainer,adMobViewModel.returnBannerAdView())

                        }

                        is AdMobAdState.AdFailedToLoad -> {
                            adViewContainer.visibility = View.GONE
                        }

                        is AdMobAdState.AdNotAvailable -> {
                            adViewContainer.visibility = View.GONE
                        }
                    }
                }

            }
        }


    }

    override fun onBackPressed() {
        finish()
    }


}