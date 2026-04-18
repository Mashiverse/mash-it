package com.mashiverse.mashit.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.mashiverse.mashit.data.models.sys.dialog.DialogContent
import com.mashiverse.mashit.data.models.sys.wallet.WalletPreferences
import com.mashiverse.mashit.data.repos.sys.DatastoreRepo
import com.mashiverse.mashit.data.repos.sys.Web3Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val web3Repo: Web3Repo,
    private val dataStoreRepo: DatastoreRepo,
) : ViewModel() {
    val walletPreferences = dataStoreRepo.walletFlow
    val firstLaunchPreferences = dataStoreRepo.firstLaunchFlow

    private val _dialogContent = mutableStateOf<DialogContent?>(null)
    val dialogContent: State<DialogContent?> = _dialogContent

    fun clearDialog() {
        _dialogContent.value = null
    }

    fun setDialogContent(dialogContent: DialogContent) {
        _dialogContent.value = dialogContent
    }

    fun updateWallet(walletPreferences: WalletPreferences) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.updateWallet(walletPreferences)
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
}