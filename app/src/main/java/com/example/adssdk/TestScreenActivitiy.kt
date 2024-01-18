package com.example.adssdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.adssdk.databinding.ActivityTestScreenActivitiyBinding
import com.example.module_ads.banner.BannerAdHelper
import com.example.module_ads.interstitial.InterstitialAdHelper
import com.example.module_ads.presentation.AdMobViewModel
import com.example.module_ads.utils.AdsConsentManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TestScreenActivitiy : AppCompatActivity() {
    private var binding: ActivityTestScreenActivitiyBinding? = null
    private val adMobViewModel: AdMobViewModel by viewModels()

    @Inject
    lateinit var interstitialAdHelper: InterstitialAdHelper
    private var adsConsentManager: AdsConsentManager? = null

    @Inject
    lateinit var bannerAdHelper: BannerAdHelper


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
                lifecycle.addObserver(bannerAdHelper)
                bannerAdHelper.loadBannerAds(
                    this@TestScreenActivitiy,
                    adViewContainer,
                    BuildConfig.ad_banner
                )
            }
        }


    }

    override fun onBackPressed() {
        finish()
    }


}