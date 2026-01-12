package io.mashit.mashit.ui.screens

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.coinbase.android.nativesdk.message.request.Account
import com.coinbase.android.nativesdk.message.request.Web3JsonRPC
import com.coinbase.android.nativesdk.message.response.ActionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import io.mashit.mashit.data.repos.DataStoreRepo
import io.mashit.mashit.data.repos.Web3Repo
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class Web3ViewModel @Inject constructor(
    private val web3Repo: Web3Repo,
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {
    val walletPreferences = dataStoreRepo.walletPreferencesFlow

    fun getCoinbaseSdk(openIntent: (Intent) -> Unit): CoinbaseWalletSDK {
        return web3Repo.getCoinbaseSdk(openIntent)
    }

    private fun updateWallet(wallet: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.updateWallet(wallet)
        }
    }

    fun disconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.removeWallet()
        }
    }

    fun initHandshake(clientRef: CoinbaseWalletSDK?) {
        val requestAccount = Web3JsonRPC.RequestAccounts().action()
        val handShakeActions = listOf(requestAccount)

        clientRef?.initiateHandshake(
            initialActions = handShakeActions
        ) { result: Result<List<ActionResult>>, account: Account? ->
            result.onSuccess { actionResults: List<ActionResult> ->
                val address = account?.address
                address?.let {
                    updateWallet(address)
                }
            }
            result.onFailure { err ->
                // TODO: Failure handling
            }
        }
    }
}