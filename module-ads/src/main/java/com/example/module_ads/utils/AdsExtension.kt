package com.example.module_ads.utils

import android.content.Context
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