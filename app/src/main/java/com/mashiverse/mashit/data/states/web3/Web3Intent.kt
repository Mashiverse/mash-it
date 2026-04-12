package com.mashiverse.mashit.data.states.web3

import com.coinbase.android.nativesdk.CoinbaseWalletSDK

sealed class Web3Intent {

    data class OnTotalSoldGet(
        val listingId: Int,
        val callback: (Int) -> Unit
    ) : Web3Intent()

    data class OnMint(
        val client: CoinbaseWalletSDK,
        val listingId: String,
        val price: Double,
        val isPolCurrency: Boolean
    ) : Web3Intent()
}