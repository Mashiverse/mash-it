package com.mashiverse.mashit.data.models.sys.wallet

data class WalletPreferences(
    val wallet: String?,
    val walletType: WalletType = WalletType.BASE
)