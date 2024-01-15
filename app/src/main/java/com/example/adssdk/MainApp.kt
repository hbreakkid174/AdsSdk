package com.example.adssdk

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.module_ads.open_ad.OpenAdHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApp :Application(), Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private lateinit var appOpenAdManager: OpenAdHelper
    private var currentActivity: Activity? = null
    fun getAppOpenAdManager()=appOpenAdManager
    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    /** ActivityLifecycleCallback methods. */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
    }
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appOpenAdManager = OpenAdHelper(applicationContext,BuildConfig.ads_open_app)
    }

    /** LifecycleObserver method that shows the app open ad when the app moves to foreground. */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
        currentActivity?.let {
            appOpenAdManager.getAppOpenAdManager().showAdIfAvailable(it) }
    }

}