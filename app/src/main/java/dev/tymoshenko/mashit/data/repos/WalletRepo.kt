package dev.tymoshenko.mashit.data.repos

import android.content.Context
import dev.tymoshenko.mashit.BuildConfig
import io.metamask.androidsdk.DappMetadata
import io.metamask.androidsdk.Ethereum
import io.metamask.androidsdk.SDKOptions
import javax.inject.Inject

class WalletRepo @Inject constructor(
    val ethereum: Ethereum
){
    fun changeEthereumChain() {
        ethereum.switchEthereumChain("0x89", {})
    }

    fun mintWithMetamask() {

    }

}