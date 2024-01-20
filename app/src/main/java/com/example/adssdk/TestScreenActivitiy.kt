package com.example.adssdk

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.adssdk.databinding.ActivityTestScreenActivitiyBinding
import com.example.module_ads.presentation.AdMobViewModel
import com.example.module_ads.utils.AdsConsentManager
import com.example.module_ads.adstates.BannerAdState
import com.example.module_ads.views.displayBannerAd
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestScreenActivitiy : AppCompatActivity() {
    private var binding: ActivityTestScreenActivitiyBinding? = null
    private val adMobViewModel: AdMobViewModel by viewModels()


    private var adsConsentManager: AdsConsentManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestScreenActivitiyBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        adsConsentManager = AdsConsentManager(this) {}
//        OpenAdHelper.enableResumeAd()
        binding?.apply {
            showAdButton.setOnClickListener {
                adMobViewModel.showNormalInterstitialAd(this@TestScreenActivitiy)
            }
            if (adsConsentManager?.canRequestAds == true) {
                adMobViewModel.loadBanner(
                    this@TestScreenActivitiy,
                    BuildConfig.ad_banner, adViewContainer
                )
                adMobViewModel.bannerAdState.observe(this@TestScreenActivitiy) {
                    when (it) {
                        is BannerAdState.AdLoaded -> {
                            displayBannerAd(adViewContainer, adMobViewModel.returnBannerAdView())

                        }

                        is BannerAdState.AdFailedToLoad -> {
                            adViewContainer.visibility = View.GONE
                        }

                        is BannerAdState.AdNotAvailable -> {
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