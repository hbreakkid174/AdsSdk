package com.example.adssdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.adssdk.databinding.ActivityTestScreenActivitiyBinding
import com.example.module_ads.interstitial.InterstitialAdHelper
import com.example.module_ads.presentation.AdMobViewModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TestScreenActivitiy : AppCompatActivity() {
    private var binding: ActivityTestScreenActivitiyBinding? = null
    private val adMobViewModel: com.example.module_ads.presentation.AdMobViewModel by viewModels()

    @Inject
    lateinit var interstitialAdHelper: InterstitialAdHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestScreenActivitiyBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.apply {
            showAdButton.setOnClickListener {
                interstitialAdHelper.showNormalInterstitialAd(
                    this@TestScreenActivitiy,
                    adMobViewModel
                )
            }
        }
    }

}