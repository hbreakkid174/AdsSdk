package com.example.adssdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.example.adssdk.databinding.ActivityTestScreenActivitiyBinding
import com.example.module_ads.interstitial.InterstitialAdHelper
import com.example.module_ads.open_ad.OpenAdHelper
import com.example.module_ads.presentation.AdMobViewModel
import com.example.module_ads.utils.AdMobAdState
import com.example.module_ads.utils.AdsConsentManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
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

                adMobViewModel.loadBanner(BuildConfig.ad_banner)
            }
            adMobViewModel.adMobAdState.observe(this@TestScreenActivitiy) {
                when (it) {
                    is AdMobAdState.AdFailedToLoad -> {
                        adViewContainer.visibility = View.GONE
                    }

                    is AdMobAdState.AdLoaded -> {
                        adViewContainer.visibility = View.VISIBLE
                        adViewContainer.addView(adMobViewModel.returnBannerView())

                    }
                }
            }
        }


    }

    override fun onBackPressed() {
        finish()
    }

    override fun onPause() {
        super.onPause()
        adMobViewModel.returnBannerView()?.pause()
    }

    override fun onResume() {
        super.onResume()
        adMobViewModel.returnBannerView()?.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        adMobViewModel.destroyBannerAd()
    }

}