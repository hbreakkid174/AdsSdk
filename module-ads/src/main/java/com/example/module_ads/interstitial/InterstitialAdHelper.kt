package com.example.module_ads.interstitial

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.module_ads.utils.FullScreenDialog
import com.example.module_ads.presentation.AdMobViewModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import javax.inject.Inject

/**
 * Helper class for managing and displaying interstitial ads.
 *
 * @property fullScreenDialog The full-screen dialog to show while loading the ad.
 * @constructor Injected constructor to provide dependencies.
 */
class InterstitialAdHelper @Inject constructor() {

    // The full-screen dialog to show while loading the ad.
    private var fullScreenDialog: FullScreenDialog? = null

    /**
     * Shows a normal interstitial ad.
     *
     * @param activity The activity where the ad will be displayed.
     * @param adMobViewModel The ViewModel managing AdMob operations.
     * @param onAdClicked Callback when the ad is clicked (optional).
     * @param onAdDismissedFullScreenContent Callback when ad is dismissed (optional).
     * @param onAdFailedToShowFullScreenContent Callback when ad fails to show (optional).
     * @param onAdImpression Callback when an impression is recorded (optional).
     * @param onAdShowedFullScreenContent Callback when ad is shown (optional).
     */
    fun showNormalInterstitialAd(
        activity: Activity,
        adMobViewModel: AdMobViewModel,
        onAdClicked: (() -> Unit)? = null,
        onAdDismissedFullScreenContent: (() -> Unit)? = null,
        onAdFailedToShowFullScreenContent: ((adError: AdError) -> Unit)? = null,
        onAdImpression: (() -> Unit)? = null,
        onAdShowedFullScreenContent: (() -> Unit)? = null
    ) {


        // Access AdMobViewModel operations.
        adMobViewModel.run {


            // Check if a normal interstitial ad is loaded.
            if (returnNormalInterstitialAd() != null) {
                // Initialize the full-screen dialog.
                fullScreenDialog = FullScreenDialog(activity)
                // Show the full-screen dialog.
                fullScreenDialog?.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    // Show the interstitial ad.
                    returnNormalInterstitialAd()?.show(activity)
                },1000)

            }

            // Set the full-screen content callback for the interstitial ad.
            returnNormalInterstitialAd()?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    // Callback triggered when the ad is clicked.
                    override fun onAdClicked() {
                        Log.d("TAG", "Ad was clicked.")
                        onAdClicked?.invoke()
                    }

                    // Callback triggered when ad is dismissed.
                    override fun onAdDismissedFullScreenContent() {
                        Log.d("TAG", "Ad dismissed fullscreen content.")
                        // Release the ad reference.
                        releaseNormalInterstitialAd()
                        // Dismiss the full-screen dialog.
                        fullScreenDialog?.dismiss()
                        fullScreenDialog=null
                        onAdDismissedFullScreenContent?.invoke()
                    }

                    // Callback triggered when ad fails to show.
                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.e("TAG", "Ad failed to show fullscreen content.")
                        // Dismiss the full-screen dialog.
                        fullScreenDialog?.dismiss()
                        fullScreenDialog=null
                        // Release the ad reference.
                        releaseNormalInterstitialAd()
                        // Invoke the callback with the error details.
                        onAdFailedToShowFullScreenContent?.invoke(adError)
                    }

                    // Callback triggered when an impression is recorded.
                    override fun onAdImpression() {
                        Log.d("TAG", "Ad recorded an impression.")
                        onAdImpression?.invoke()
                    }

                    // Callback triggered when the ad is shown.
                    override fun onAdShowedFullScreenContent() {
                        Log.d("TAG", "Ad showed fullscreen content.")
                        // Dismiss the full-screen dialog.
                        fullScreenDialog?.dismiss()
                        fullScreenDialog=null
                        onAdShowedFullScreenContent?.invoke()
                    }
                }
        }
    }
}