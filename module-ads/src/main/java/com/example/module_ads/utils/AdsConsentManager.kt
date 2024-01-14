package com.example.module_ads.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast

class AdsConsentManager(private val context: Activity,onConsentFound:()->Unit) {
    private var googleMobileAdsConsentManager: GoogleMobileAdsConsentManager =
        GoogleMobileAdsConsentManager.getInstance(context)

    init {
        googleMobileAdsConsentManager.gatherConsent(context) { consentError ->
            if (consentError != null) {
                // Consent not obtained in current session.
                debug("Consent: ${consentError.errorCode}: ${consentError.message}")
            }
            else{
                onConsentFound()
                debug("Consent obtained in current session.")
            }
        }
    }

    val canRequestAds = googleMobileAdsConsentManager.canRequestAds
    val isPrivacyOptionsRequired = googleMobileAdsConsentManager.isPrivacyOptionsRequired

    fun showPrivacyForm(invoke:()->Unit){
        googleMobileAdsConsentManager.showPrivacyOptionsForm(context) { formError ->
            if (formError != null) {
              debug("formError: ${ formError.message}")
            }
            invoke()
        }
    }
}