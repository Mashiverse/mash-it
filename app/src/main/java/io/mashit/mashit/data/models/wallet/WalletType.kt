package io.mashit.mashit.data.models.wallet

enum class WalletType {
    METAMASK,
    COINBASE;
}

fun String.toWalletTypeEnum() = WalletType.valueOf(this)