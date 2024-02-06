package com.example.adssdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adssdk.databinding.ActivityInAppPurchaseBinding
import com.example.module_ads.billing.InAppConsumableBilling
import com.example.module_ads.views.debug

class InAppPurchaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInAppPurchaseBinding
    private var oneTimePurchaseProductId = "android.test.purchased"
    private var inAppConsumableBilling: InAppConsumableBilling? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInAppPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inAppConsumableBilling = InAppConsumableBilling(this, productId = oneTimePurchaseProductId)
        binding.apply {
            purchaseApp.setOnClickListener {
                inAppConsumableBilling?.purchaseItem()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}