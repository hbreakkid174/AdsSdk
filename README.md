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

## Installation

1. Add Google AdMob dependencies to your `build.gradle`:

```gradle
implementation 'com.google.android.gms:play-services-ads:21.1.0'
