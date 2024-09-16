package com.example.module_ads.di

import android.content.Context
import com.example.module_ads.data.data.BannerAdRepositoryImpl
import com.example.module_ads.domain.repositories.InterstitialAdRepository
import com.example.module_ads.data.data.InterstitialAdRepositoryImpl
import com.example.module_ads.data.data.NativeAdRepositoryImpl
import com.example.module_ads.domain.repositories.BannerAdRepository
import com.example.module_ads.domain.repositories.NativeAdRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides the application context.
     *
     * @param context The application context.
     * @return The application context.
     */
    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    /**
     * Provides the implementation of [InterstitialAdRepository] for AdMob interstitial ads.
     *
     * @param context The application context.
     * @return The implementation of [InterstitialAdRepository].
     */
    @Provides
    @Singleton
    fun provideAdMobRepository(context: Context): InterstitialAdRepository {
        return InterstitialAdRepositoryImpl(context)
    }
    /**
     * Provides the implementation of [BannerAdRepository] for AdMob banner ads.
     *
     * @return The implementation of [BannerAdRepository].
     */
    @Provides
    @Singleton
    fun provideBannerAdRepository(): BannerAdRepository {
        return BannerAdRepositoryImpl()
    }
    /**
     * Provides the implementation of [NativeAdRepository] for AdMob native ads.
     *
     * @return The implementation of [NativeAdRepository].
     */
    @Provides
    @Singleton
    fun provideNativeAdRepository(): NativeAdRepository {
        return NativeAdRepositoryImpl()
    }
}
