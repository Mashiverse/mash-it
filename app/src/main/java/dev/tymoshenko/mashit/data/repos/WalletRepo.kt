package dev.tymoshenko.mashit.data.repos

import io.metamask.androidsdk.Ethereum
import javax.inject.Inject

class WalletRepo @Inject constructor(
    val ethereum: Ethereum
) {
    fun changeEthereumChain() {
        ethereum.switchEthereumChain("0x89", {})
    }

    fun mintWithMetamask() {

    }

}