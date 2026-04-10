package com.mashiverse.mashit.data.intents

import android.content.Intent
import com.coinbase.android.nativesdk.CoinbaseWalletSDK

sealed class Web3Intent {

    data class OnCoinbaseGet(
        val onOpen: (Intent) -> Unit
    ) : Web3Intent()

    data class OnTotalSoldGet(
        val listingId: Int,
        val callback: (Int) -> Unit
    ) : Web3Intent()

    data class OnMint(
        val client: CoinbaseWalletSDK,
        val from: String,
        val listingId: String,
        val price: Double
    ) : Web3Intent()
}