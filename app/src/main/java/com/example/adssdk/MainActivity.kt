package com.example.adssdk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.adssdk.databinding.ActivityMainBinding
import com.example.module_ads.interstitial.InterstitialAdHelper
import com.example.module_ads.presentation.AdMobViewModel
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
        adsConsentManager = AdsConsentManager(this) {
            initSdk()
        }
        if (adsConsentManager?.canRequestAds == true) {
            initSdk()
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
//            adMobViewModel.adMobAdState.observe(this@MainActivity) {
//                when (it) {
//                    is com.example.module_ads.utils.AdMobAdState.AdFailedToLoad -> {
//                        Log.d(TAG, "Ad Failed to load1")
//                    }
//
//                    is com.example.module_ads.utils.AdMobAdState.AdLoaded -> {
//                        Log.d(TAG, "Ad Loaded1")
//                        showAdButton.isEnabled = true
//
//                    }
//                }
//            }
        }


    }

    private fun initSdk() {
        initializeAdmobSdk {
            Log.d(TAG, "onCreate: initializeAdmobSdk")
        }
    }
}
