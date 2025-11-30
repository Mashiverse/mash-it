package dev.tymoshenko.mashit.ui.screens.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tymoshenko.mashit.data.repos.DataStoreRepo
import jakarta.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val dataStoreRepo: DataStoreRepo
) : ViewModel() {
    val walletPreferences = dataStoreRepo.walletPreferencesFlow

    fun connectMetamask() {
        // TODO
    }

    fun connectCoinbase() {
        // TODO
    }
}