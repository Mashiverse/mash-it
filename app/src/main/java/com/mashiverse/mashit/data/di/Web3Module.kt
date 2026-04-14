package com.mashiverse.mashit.data.di

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.reown.android.Core
import com.reown.appkit.client.Modal
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
        return { openIntent ->
            CoinbaseWalletSDK(
                appContext = ctx,
                domain = "https://mash-it.io".toUri(),
                openIntent = openIntent
            )
        }
    }

    @Provides
    @Singleton
    fun provideAppMetaData(): Core.Model.AppMetaData = Core.Model.AppMetaData(
        name = "mash-it",
        description = "Combine mashable nfts to get the perfect mashi",
        url = "https://www.mash-it.io",
        icons = listOf("https://www.mash-it.io/img/transparent_logo_p.png?v=4e7c0f250868de67bf09a2c36db38666fdb151e2"),
        redirect = "com.mashiverse.mashit://request",
    )

    @Provides
    @Singleton
    fun providePolygonChain(): Modal.Model.Chain = Modal.Model.Chain(
        chainName = "Polygon",
        chainNamespace = "eip155",
        chainReference = "137",
        requiredMethods = listOf("eth_sendTransaction", "personal_sign"),
        optionalMethods = emptyList(),
        events = listOf("chainChanged", "accountsChanged"),
        token = Modal.Model.Token(name = "USD Coin", symbol = "USDC", decimal = 6),
        rpcUrl = "https://polygon-rpc.com",
        blockExplorerUrl = "https://polygonscan.com"
    )
}