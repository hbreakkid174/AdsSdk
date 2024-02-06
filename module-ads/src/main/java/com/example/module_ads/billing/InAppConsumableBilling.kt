package com.example.module_ads.billing

import android.app.Activity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.example.module_ads.views.debug


class InAppConsumableBilling(
    private val activity: Activity,
    private val productId: String
) {
    private var billingClient: BillingClient? = null

    init {
        billingClient = BillingClient.newBuilder(activity)
            .enablePendingPurchases()
            .setListener { billingResult: BillingResult, list: List<Purchase?>? ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && list != null) {
                    for (purchase in list) {
                        debug("Billing->Response is OK")
                        purchase?.let { handlePurchase(it) }
                    }
                } else {
                    debug("Billing->Response NOT OK")
                }
            }.build()
        establishConnection()
    }

    private fun establishConnection() {
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {


                    debug("Billing->Connection Established")
                }
            }

            override fun onBillingServiceDisconnected() {

                debug("Billing->Connection NOT Established")
                establishConnection()
            }
        })
    }

    fun purchaseItem() {
        val productList = ArrayList<QueryProductDetailsParams.Product>()
        productList.add(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient?.queryProductDetailsAsync(
            params
        ) { billingResult, list ->
            debug("Billing->billingResult: ${billingResult.debugMessage}")
            launchPurchaseFlow(list[0])
        }
    }

    private fun launchPurchaseFlow(productDetails: ProductDetails?) {
        val productList = ArrayList<ProductDetailsParams>()

        productList.add(
            ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails!!)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productList)
            .build()

        billingClient?.launchBillingFlow(activity, billingFlowParams)
    }

    private fun handlePurchase(purchases: Purchase) {
        if (!purchases.isAcknowledged) {
            billingClient!!.acknowledgePurchase(
                AcknowledgePurchaseParams
                    .newBuilder()
                    .setPurchaseToken(purchases.purchaseToken)
                    .build()
            ) { billingResult: BillingResult ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    for (pur in purchases.products) {
                        if (pur.equals(productId, ignoreCase = true)) {
                            debug("Billing->Purchase is successful")


                            consumePurchase(purchases)
                        }
                    }
                }
            }
        }
    }

    private fun consumePurchase(purchases: Purchase) {
        val params = ConsumeParams.newBuilder()
            .setPurchaseToken(purchases.purchaseToken)
            .build()
        billingClient?.consumeAsync(
            params
        ) { billingResult, s ->
            debug("Billing result consume: ${billingResult.debugMessage}")
            debug("Billing Consuming Successful: $s")
            debug("Billing->Product Consumed")
        }
    }


    fun restorePurchases() {
        billingClient = BillingClient.newBuilder(activity).enablePendingPurchases()
            .setListener { billingResult: BillingResult?, list: List<Purchase?>? ->
                debug("Billing result: ${billingResult?.debugMessage}")
                list?.forEach {
                    debug("Billing Purchase->products: ${it?.products}")
                }
            }
            .build()
        val finalBillingClient: BillingClient = billingClient as BillingClient
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                debug("Billing service disconnected")
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    finalBillingClient.queryPurchasesAsync(
                        QueryPurchasesParams.newBuilder()
                            .setProductType(BillingClient.ProductType.INAPP).build()
                    ) { billingResult1: BillingResult, list: List<Purchase> ->
                        if (billingResult1.responseCode == BillingClient.BillingResponseCode.OK) {
                            if (list.isNotEmpty()) {
                                debug("Billing IN APP SUCCESS RESTORE: $list")
                                for (i in list.indices) {
                                    if (list[i].products.contains(productId)) {
                                        debug("Billing Premium Restored")
                                        debug("Billing Product id $productId will restore here")
                                    }
                                }
                            } else {
                                debug("Billing->Nothing found to Restored")
                                debug("Billing->In APP Not Found To Restore")
                            }
                        }
                    }
                }
            }
        })
    }
}