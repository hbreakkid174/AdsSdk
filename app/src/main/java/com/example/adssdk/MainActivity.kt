package com.example.adssdk

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.adssdk.databinding.ActivityMainBinding
import com.example.module_ads.interstitial.InterstitialAdHelper
import com.example.module_ads.presentation.AdMobViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val adMobViewModel: AdMobViewModel by viewModels()
    private var binding: ActivityMainBinding? = null

    @Inject
    lateinit var interstitialAdHelper: InterstitialAdHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.apply {
            loadAdButton.setOnClickListener {
                adMobViewModel.loadNormalInterstitialAd(BuildConfig.ad_interstitial)
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
//                    is com.example.module_ads.AdMobAdState.AdFailedToLoad -> {
//                        Log.d("TAG", "Ad Failed to load1")
//                    }
//
//                    is com.example.module_ads.AdMobAdState.AdLoaded -> {
//                        Log.d("TAG", "Ad Loaded1")
//                        showAdButton.isEnabled = true
//
//                    }
//                }
//            }
        }

    }


}