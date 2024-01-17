package com.example.adssdk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.adssdk.databinding.ActivityMainBinding
import com.example.module_ads.enums.CollapsibleBannerPosition
import com.example.module_ads.interstitial.InterstitialAdHelper
import com.example.module_ads.open_ad.OpenAdHelper
import com.example.module_ads.presentation.AdMobViewModel
import com.example.module_ads.utils.AdMobAdState
import com.example.module_ads.utils.AdsConsentManager
import com.example.module_ads.utils.initializeAdmobSdk
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val adMobViewModel: AdMobViewModel by viewModels()
    private var binding: ActivityMainBinding? = null

    companion object {
        private var TAG = "MainActivity"
    }

    @Inject
    lateinit var interstitialAdHelper: InterstitialAdHelper
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
                interstitialAdHelper.showNormalInterstitialAd(
                    this@MainActivity,
                    adMobViewModel
                )
            }
            nextButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, TestScreenActivitiy::class.java))

            }
loadCollapsibleBanner()
        }


    }

    private fun loadCollapsibleBanner() {

        binding?.apply {
            if (adsConsentManager?.canRequestAds == true) {

                adMobViewModel.loadCollapsibleBanner(BuildConfig.ad_banner_collapsible,bannerContainer,CollapsibleBannerPosition.BOTTOM)
            }
            adMobViewModel.adMobAdState.observe(this@MainActivity) {
                when (it) {
                    is AdMobAdState.AdFailedToLoad -> {
                        bannerContainer.removeAllViews()
                        bannerContainer.visibility = View.GONE

                    }

                    is AdMobAdState.AdLoaded -> {
//                        bannerContainer.removeAllViews()
                        bannerContainer.addView(adMobViewModel.returnBannerView())

                    }

                    is AdMobAdState.AdNotAvailable -> {
                        bannerContainer.removeAllViews()
                        bannerContainer.visibility = View.GONE


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

    override fun onDestroy() {
        super.onDestroy()
        adMobViewModel.destroyBannerAd()
    }

    override fun onPause() {
        super.onPause()
        adMobViewModel.pauseBannerAd()
    }

    override fun onResume() {
        super.onResume()
        adMobViewModel.resumeBannerAd()
    }
}
