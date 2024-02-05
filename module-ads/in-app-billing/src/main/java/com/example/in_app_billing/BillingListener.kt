package com.example.in_app_billing

import com.android.billingclient.api.*
import com.example.in_app_billing.BillingEvent

interface BillingListener {
    /**
     * @param event one of resolved [BillingEvent]s
     * @param message as reported by [BillingResult.getDebugMessage]
     * @param responseCode as reported by [BillingResult.getResponseCode] as [BillingClient.BillingResponseCode].
     */
    fun onBillingEvent(event: BillingEvent, message: String?, responseCode: Int?)
}