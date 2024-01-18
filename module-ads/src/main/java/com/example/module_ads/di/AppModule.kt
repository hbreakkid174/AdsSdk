package com.example.module_ads.di

import android.content.Context
import com.example.module_ads.domain.InterstitialAdRepository
import com.example.module_ads.data.InterstitialAdRepositoryImpl
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

}
