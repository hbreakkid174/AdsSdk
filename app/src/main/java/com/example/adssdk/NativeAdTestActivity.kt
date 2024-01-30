package com.example.adssdk

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.adssdk.databinding.ActivityNativeAdTestBinding
import com.example.module_ads.domain.repositories.NativeAdRepository
import com.example.module_ads.enums.NativeAdType
import com.example.module_ads.presentation.AdMobViewModel
import com.example.module_ads.utils.AdsConsentManager
import com.example.module_ads.views.debug
import com.google.android.gms.ads.LoadAdError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NativeAdTestActivity : AppCompatActivity() {
    private var binding: ActivityNativeAdTestBinding? = null
    private val adMobViewModel: AdMobViewModel by viewModels()
    private var adsConsentManager: AdsConsentManager? = null
    override fun onBackPressed() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeAdTestBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        adsConsentManager = AdsConsentManager(this) {}
        binding?.apply {
            if (adsConsentManager?.canRequestAds == true) {
                adMobViewModel.loadNativeAd(this@NativeAdTestActivity, BuildConfig.ad_native,
                    object :NativeAdRepository.NativeAdLoadCallback{
                        override fun onNativeAdLoaded() {
                            debug("NativeAdState->load")
                            //medium native ad
//                            adMobViewModel.populateNativeAdView(
//                                this@NativeAdTestActivity,
//                                nativeAdContainerMedium,
//                                NativeAdType.MEDIUM
//                            )
                            //small native ad
//                            adMobViewModel.populateNativeAdView(
//                                this@NativeAdTestActivity,
//                                nativeAdContainerSmall,
//                                NativeAdType.SMALL
//                            )
                            //native ad banner
                            adMobViewModel.populateNativeAdView(
                                this@NativeAdTestActivity,
                                nativeAdContainerBanner,
                                NativeAdType.NATIVE_BANNER
                            )
                            //large native ad
//                            adMobViewModel.populateNativeAdView(
//                                this@NativeAdTestActivity,
//                                nativeAdContainerLarge,
//                                NativeAdType.LARGE
//                            )

                        }

                        override fun onNativeAdNotAvailable() {
                            debug("NativeAdState->not available")
                            //medium native ad
//                            manageFrameLayoutView(nativeAdContainerMedium)

                            //small native ad
//                            manageFrameLayoutView(nativeAdContainerSmall)
                            //native ad banner
                            manageFrameLayoutView(nativeAdContainerBanner)
                            //large native ad
//                            manageFrameLayoutView(nativeAdContainerLarge)
                        }

                        override fun onNativeAdFailedToLoad(loadAdError: LoadAdError) {
                            debug("NativeAdState->failed: ${loadAdError.message}")
                            //medium native ad

//                            manageFrameLayoutView(nativeAdContainerMedium)
                            //small native ad
//                            manageFrameLayoutView(nativeAdContainerSmall)
                            //native ad banner
                            manageFrameLayoutView(nativeAdContainerBanner)
                            //large native ad
//                            manageFrameLayoutView(nativeAdContainerLarge)
                        }
                    })
            }

        }

    }

    private fun manageFrameLayoutView(frameLayout: FrameLayout) {
        frameLayout.removeAllViews()
        frameLayout.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        adMobViewModel.destroyNativeAd()
    }
}