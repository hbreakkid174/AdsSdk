package com.example.module_ads.views

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.example.module_ads.BuildConfig
import com.google.android.gms.ads.AdView

private var toast: Toast? = null

//for short toast
fun Context.toast(message: String) {
    if (toast != null) {
        toast?.cancel()
        toast = null
    }
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    if (BuildConfig.DEBUG) {
        toast?.show()
    }
}


fun debug(text: String) {
    if (BuildConfig.DEBUG) {
        Log.d("AdsModule", text)
    }
}


 fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val networkInfo = connectivityManager.getNetworkCapabilities(networkCapabilities)

    return networkInfo?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true ||
            networkInfo?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true ||
            networkInfo?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
}

 fun displayBannerAd(adsPlaceHolder: FrameLayout,adaptiveAdView:AdView?) {
    try {
        if (adaptiveAdView != null) {
            val viewGroup: ViewGroup? = adaptiveAdView.parent as ViewGroup?
            viewGroup?.removeView(adaptiveAdView)

            adsPlaceHolder.removeAllViews()
            adsPlaceHolder.addView(adaptiveAdView)
        } else {
            adsPlaceHolder.removeAllViews()
            adsPlaceHolder.visibility = View.GONE
        }
    } catch (ex: Exception) {
       ex.printStackTrace()
    }

}
