package com.example.adssdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        (application as MainApp).getAppOpenAdManager().loadAd(this)

        lifecycleScope.launchWhenResumed {
            delay(3000)
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finish()
        }
    }
}