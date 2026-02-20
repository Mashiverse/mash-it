package com.mashiverse.mashit.di

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Web3Module {

    @Provides
    @Singleton
    fun provideCoinbaseSdkFactory(
        @ApplicationContext ctx: Context
    ): @JvmSuppressWildcards (openIntent: (Intent) -> Unit) -> CoinbaseWalletSDK {
        return  { openIntent ->
            CoinbaseWalletSDK(
                appContext = ctx,
                domain = "https://mash-it.io".toUri(),
                openIntent = openIntent
            )
        }
    }
}