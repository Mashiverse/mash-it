package dev.tymoshenko.mashit.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.tymoshenko.mashit.BuildConfig
import io.metamask.androidsdk.DappMetadata
import io.metamask.androidsdk.Ethereum
import io.metamask.androidsdk.SDKOptions
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Web3Module {

    @Provides
    @Singleton
    fun provideEthereum(
        @ApplicationContext ctx: Context
    ): Ethereum {
        val dappMetadata = DappMetadata("Mash It", "https://www.mash-it.io")
        val infuraApiKey = BuildConfig.INFURA_API_KEY
        val readonlyRpcMap = mapOf("0x1" to "https://polygon-rpc.com")

        return Ethereum(
            ctx,
            dappMetadata = dappMetadata,
            sdkOptions = SDKOptions(infuraAPIKey = infuraApiKey, readonlyRPCMap = readonlyRpcMap)
        )
    }
}