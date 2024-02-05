package com.example.adssdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adssdk.databinding.ActivityInAppPurchaseBinding
import com.example.in_app_billing.BillingEvent
import com.example.in_app_billing.BillingHelper
import com.example.in_app_billing.BillingListener
import com.example.module_ads.views.debug

class InAppPurchaseActivity : AppCompatActivity(), BillingListener {
    private lateinit var binding: ActivityInAppPurchaseBinding
    private lateinit var billingHelper: BillingHelper
    private var oneTimePurchaseProductId = "android.test.purchased"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInAppPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        billingHelper = BillingHelper(
            this,
            productInAppPurchases = listOf(oneTimePurchaseProductId),
            productSubscriptions = null,
            enableLogging = true,
            billingListener = this

        )
        binding.apply {
            purchaseApp.setOnClickListener {
                billingHelper.launchPurchaseFlow(
                    this@InAppPurchaseActivity,
                    oneTimePurchaseProductId
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        billingHelper.endClientConnection()
    }

    override fun onBillingEvent(event: BillingEvent, message: String?, responseCode: Int?) {
        debug("onBillingEvent>>event: $event")
        debug("onBillingEvent>>message: $message")
        debug("onBillingEvent>>responseCode: $responseCode")
    }
}