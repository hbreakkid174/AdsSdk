package com.example.module_ads.utils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.example.module_ads.views.debug
import com.example.module_ads.views.toast
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import java.util.concurrent.atomic.AtomicBoolean

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
            context.toast("initialize Mobile Ads Admob...")
            debug("initialize Mobile Ads Admob...")

            // Set your test devices. Check your logcat output for the hashed device ID to
            // get test ads on a physical device. e.g.
            // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
            // to get test ads on this device."
            MobileAds.setRequestConfiguration(
                RequestConfiguration.Builder().setTestDeviceIds(listOf("ABCDEF012345")).build()
            )

        }
    }
}

fun Fragment.initializeAdmobSdk(block: InitializeAdmobSdk.Builder.() -> Unit): InitializeAdmobSdk {
    return InitializeAdmobSdk.Builder(requireContext()).apply(block).build()
}

fun Activity.initializeAdmobSdk(block: InitializeAdmobSdk.Builder.() -> Unit): InitializeAdmobSdk {
    return InitializeAdmobSdk.Builder(this).apply(block).build()
}
