plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.adssdk"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.adssdk"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    flavorDimensions("default")
    productFlavors {
        create("debugState") {
            //use id test when dev

            manifestPlaceholders ["ad_app_id"]= "ca-app-pub-3940256099942544~3347511713"
            buildConfigField("String", "ad_interstitial", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "ad_banner", "\"ca-app-pub-3940256099942544/9214589741\"")
            buildConfigField("String", "ad_banner_collapsible", "\"ca-app-pub-3940256099942544/2014213617\"")
            buildConfigField ("String", "ad_reward", "\"ca-app-pub-3940256099942544/5224354917\"")
            buildConfigField ("String", "ad_reward_inter", "\"ca-app-pub-3940256099942544/5354046379\"")
            buildConfigField ("String", "ad_native", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField ("String", "ads_open_app", "\"ca-app-pub-3940256099942544/9257395921\"")
            buildConfigField ("Boolean", "env_dev", "true")
            // and add rest of ids as per requirements

        }
        create("productionState") {
            //add your id ad here
            manifestPlaceholders ["ad_app_id"]= "ad your real ads id here"
            buildConfigField("String", "ad_interstitial", "\"ad your real ads id here\"")
            buildConfigField("String", "ad_banner", "\"ad your real ads id here\"")
            buildConfigField("String", "ad_banner_collapsible", "\"ad your real ads id here\"")
            buildConfigField ("String", "ad_reward", "\"ad your real ads id here\"")
            buildConfigField ("String", "ad_reward_inter", "\"ad your real ads id here\"")
            buildConfigField ("String", "ad_native", "\"ad your real ads id here\"")
            buildConfigField ("String", "ads_open_app", "\"ad your real ads id here\"")
            buildConfigField ("Boolean", "env_dev", "false")
            // and add rest of ids as per requirements
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
       viewBinding = true
        buildConfig =true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    api(project(":module-ads"))
    api(project(":module-ads:in-app-billing"))


//Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.42")
    kapt("com.google.dagger:hilt-android-compiler:2.42")
}