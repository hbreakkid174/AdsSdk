# Interstitial Ad Management Library

This library provides a simple and flexible implementation for loading, showing, and managing interstitial ads in Android applications. It follows a clean architecture approach, separating concerns between the repository layer and the use case layer.

## Features

- Load interstitial ads with callback support.
- Show interstitial ads with appropriate lifecycle management.
- Handle network availability and purchase state to manage ad display conditions.
- Implement a full callback system for handling ad load, failure, impressions, clicks, and dismissals.

## Architecture

The architecture of this library follows the **Clean Architecture** pattern:
- **Use Case Layer**: Manages business logic for loading and showing ads.
- **Repository Layer**: Handles interactions with external ad services (like Google's AdMob SDK).

## Usage

### 1. InterstitialAdRepository Interface

This interface defines the core operations required to load, show, and manage interstitial ads. It includes the following methods:

- **Load an interstitial ad**: Initiates the loading of an interstitial ad with the given ad information and purchase state.
- **Return a loaded interstitial ad instance**: Fetches the loaded interstitial ad instance, or returns `null` if the ad is not available.
- **Release ad resources**: Frees up the resources tied to the interstitial ad when it is no longer needed.
- **Show an interstitial ad**: Displays the interstitial ad in the provided activity context.

```kotlin
interface InterstitialAdRepository {
    fun loadNormalInterstitialAd(
        adInfo: InterstitialAdInfo, 
        isPurchased: Boolean, 
        callback: InterstitialAdLoadCallback
    )

    fun returnNormalInterstitialAd(adKey: Int): InterstitialAdInfo?

    fun releaseNormalInterstitialAd(adKey: Int)

    fun showNormalInterstitialAd(
        adInfo: InterstitialAdInfo, 
        isPurchased: Boolean, 
        activity: Activity, 
        callback: InterstitialAdLoadCallback
    )
}


