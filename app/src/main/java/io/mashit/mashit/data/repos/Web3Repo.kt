package io.mashit.mashit.data.repos

import android.content.Context
import android.content.Intent
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import javax.inject.Inject

class Web3Repo @Inject constructor(
    private val coinbaseSdkFactory: @JvmSuppressWildcards (openIntent: (Intent) -> Unit) -> CoinbaseWalletSDK
) {
    fun getCoinbaseSdk(openIntent: (Intent) -> Unit): CoinbaseWalletSDK {
        return coinbaseSdkFactory.invoke(openIntent)
    }
}