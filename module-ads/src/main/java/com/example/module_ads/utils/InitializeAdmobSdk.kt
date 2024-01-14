package com.example.module_ads.utils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class InitializeAdmobSdk private constructor(private val context: Context) {

    private val isMobileAdsInitializeCalled = AtomicBoolean(false)

    class Builder(private val context: Context) {
        private var onInitCallback: (() -> Unit)? = null

        fun onInit(callback: () -> Unit) {
            onInitCallback = callback
        }

        fun build(): InitializeAdmobSdk {
            return InitializeAdmobSdk(context, onInitCallback).apply { onInitCallback?.invoke() }
        }
    }

    private constructor(context: Context, onInitCallback: (() -> Unit)?) : this(context) {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(context) {
            onInitCallback?.invoke()
            // Load an ad or any other initialization logic.
        }
    }
}

fun Fragment.initializeAdmobSdk(block: InitializeAdmobSdk.Builder.() -> Unit): InitializeAdmobSdk {
    return InitializeAdmobSdk.Builder(requireContext()).apply(block).build()
}
fun Activity.initializeAdmobSdk(block: InitializeAdmobSdk.Builder.() -> Unit): InitializeAdmobSdk {
    return InitializeAdmobSdk.Builder(this).apply(block).build()
}
