package com.example.adssdk

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.adssdk.databinding.ActivityTestScreenActivitiyBinding
import com.example.module_ads.domain.repositories.BannerAdRepository
import com.example.module_ads.presentation.AdMobViewModel
import com.example.module_ads.utils.AdsConsentManager
import com.example.module_ads.domain.repositories.InterstitialAdRepository
import com.example.module_ads.views.debug
import com.example.module_ads.views.displayBannerAd
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TestScreenActivitiy : AppCompatActivity() {
    private var binding: ActivityTestScreenActivitiyBinding? = null
    private val adMobViewModel: AdMobViewModel by viewModels()


    private var adsConsentManager: AdsConsentManager? = null
    @Inject
    lateinit var breakInfoArrayList: ArrayList<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestScreenActivitiyBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        adsConsentManager = AdsConsentManager(this) {}
        debug("breakInfoArrayList: ${breakInfoArrayList.size}")
//        OpenAdHelper.enableResumeAd()
        binding?.apply {
            showAdButton.setOnClickListener {
//                adMobViewModel.showNormalInterstitialAd(this@TestScreenActivitiy,object :InterstitialAdRepository.InterstitialAdLoadCallback{
//                    override fun onInterstitialAdNotAvailable() {
//
//                    }
//
//                })
            }
            if (adsConsentManager?.canRequestAds == true) {
                adMobViewModel.loadBanner(
                    this@TestScreenActivitiy,
                    BuildConfig.ad_banner, adViewContainer,
                    object : BannerAdRepository.BannerAdLoadCallback {
                        override fun onBannerAdLoaded() {
                            displayBannerAd(adViewContainer, adMobViewModel.returnBannerAdView())

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
        adMobViewModel.resumeBannerAd()
    }
    override fun onPause() {
        super.onPause()
        adMobViewModel.pauseBannerAd()
    }
    override fun onDestroy() {
        super.onDestroy()
        adMobViewModel.destroyBannerAd()
    }
    override fun onBackPressed() {
        finish()
    }


}