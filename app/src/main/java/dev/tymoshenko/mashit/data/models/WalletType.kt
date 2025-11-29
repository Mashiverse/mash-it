package dev.tymoshenko.mashit.data.models

enum class WalletType {
    METAMASK,
    COINBASE;
}

fun String.toWalletTypeEnum() = WalletType.valueOf(this)