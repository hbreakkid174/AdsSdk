@file:Suppress("unused")

package com.example.in_app_billing

import android.util.Log
import com.android.billingclient.api.ProductDetails
import java.util.regex.Pattern
import kotlin.math.roundToLong

object PriceUtil {
    internal const val TAG = "BillingHelper/PriceUtil"

    var enableLogging: Boolean = false

    /**
     * Convert a formatted price to a formatted price with a given divider.
     * For example: "12.80 EUR" with a divider of 4 will return "4.20 EUR"
     *
     * @param price formatted price input
     * @param divider divide the extracted price
     * @return formatted divided price or null on error
     */
    fun convertFullPriceToDivided(
        price: String,
        divider: Int
    ): String? {
        var fullPrice = price
        if (divider == 1) {
            return price
        } else try {
            // Must use javaSpaceChar and not a typed space
            fullPrice = fullPrice.replace("(?<=\\d)\\p{javaSpaceChar}+(?=\\d)".toRegex(), "").trim()
            fullPrice =
                if (fullPrice.contains(",") && fullPrice.contains(".") && fullPrice.last() != '.')
                    fullPrice.replace(",", "")
                else fullPrice.replace(",", ".")

            var digit: String? = null
            val currency: String
            val regex = Pattern.compile("(\\d+(?:\\.\\d+)?)")
            val matcher = regex.matcher(fullPrice)
            while (matcher.find()) {
                digit = matcher.group(1)
            }
            if (digit != null) {
                currency = fullPrice.replace(digit, "")
                val digitValue = digit.toDouble() / divider

                return if (fullPrice.startsWith(currency)) currency + roundDigitString(digitValue)
                else roundDigitString(digitValue) + currency
            }
        } catch (e: Exception) {
            if (enableLogging) Log.e(TAG, e.message ?: "")
        }

        return null
    }

    private fun roundDigitString(digitValue: Double): String {
        val priceValueString =
            if (digitValue > 1000.0) digitValue.roundToLong().toString() else
                String.format("%.2f", digitValue)
        return priceValueString.replace(".", ",")
    }
}


/**
 * A helper method making it easier to retrieve a formatted price from product details.
 * Parameters only apply to subscription products.
 *
 * @param subscriptionOfferIndex index of [ProductDetails.getSubscriptionOfferDetails].
 * @param subscriptionPricingPhaseIndex index of [ProductDetails.SubscriptionOfferDetails.pricingPhases]
 * @return formatted price or null on error
 */
fun ProductDetails.getFormattedPrice(
    subscriptionOfferIndex: Int = 0,
    subscriptionPricingPhaseIndex: Int = 0
): String? {
    return if (isInAppPurchase()) {
        oneTimePurchaseOfferDetails?.formattedPrice
    } else try {
        if (isSubscription()) {
            subscriptionOfferDetails?.getOrNull(subscriptionOfferIndex)
                ?.pricingPhases
                ?.pricingPhaseList?.getOrNull(subscriptionPricingPhaseIndex)
                ?.formattedPrice
        } else {
            null
        }
    } catch (e: Exception) {
        if (PriceUtil.enableLogging) Log.e(PriceUtil.TAG, e.message ?: "")
        null
    }
}