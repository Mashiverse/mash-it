package com.mashiverse.mashit.ui.screens

import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.coinbase.android.nativesdk.message.request.Account
import com.coinbase.android.nativesdk.message.request.Web3JsonRPC
import com.coinbase.android.nativesdk.message.response.ActionResult
import com.google.firebase.messaging.FirebaseMessaging
import com.mashiverse.mashit.data.models.dialog.DialogContent
import com.mashiverse.mashit.data.repos.DatastoreRepo
import com.mashiverse.mashit.data.repos.Web3Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.web3j.abi.datatypes.Bool
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val web3Repo: Web3Repo,
    private val dataStoreRepo: DatastoreRepo,
) : ViewModel() {
    val walletPreferences = dataStoreRepo.walletPreferencesFlow
    val firstLaunchPreferences = dataStoreRepo.firstLaunchPreferencesFlow

    private val _dialogContent = mutableStateOf<DialogContent?>(null)
    val dialogContent: State<DialogContent?> = _dialogContent

    fun clearDialog() {
        _dialogContent.value = null
    }

    fun setDialogContent(dialogContent: DialogContent) {
        _dialogContent.value = dialogContent
    }

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

    fun setFirstLaunchCompleted() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.setFirstLaunchCompleted()
        }
    }

    fun updateNotifications(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (enabled) {
                FirebaseMessaging.getInstance().subscribeToTopic("all_users")
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("all_users")
            }

            dataStoreRepo.updateNotifications(enabled)
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
                _dialogContent.value = DialogContent(
                    title = "Base auth error",
                    text = err.message ?: "Unknown error"
                )
            }
        }
    }
}