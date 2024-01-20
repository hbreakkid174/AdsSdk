package com.example.module_ads.utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.module_ads.views.debug
import com.google.android.gms.ads.AdView

class CollapsedAdViewLifecycleObserver(
    private val collapsedAdView: AdView?,
    val setCollapsedViewToNull: () -> Unit
) : DefaultLifecycleObserver {

    override fun onResume(owner: LifecycleOwner) {
        debug("resuming collapse banner")
        collapsedAdView?.resume()
    }

    override fun onPause(owner: LifecycleOwner) {
        debug("pausing collapse banner")

        collapsedAdView?.pause()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        debug("destroying collapse banner")
        setCollapsedViewToNull()
        collapsedAdView?.destroy()
    }
}
