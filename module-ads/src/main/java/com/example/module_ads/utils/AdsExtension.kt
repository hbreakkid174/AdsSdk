package com.example.module_ads.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.module_ads.BuildConfig

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


private fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val networkInfo = connectivityManager.getNetworkCapabilities(networkCapabilities)

    return networkInfo?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true ||
            networkInfo?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true ||
            networkInfo?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
}