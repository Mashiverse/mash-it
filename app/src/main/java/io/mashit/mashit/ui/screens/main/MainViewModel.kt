package io.mashit.mashit.ui.screens.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.mashit.mashit.data.repos.WalletRepo
import jakarta.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val walletRepo: WalletRepo
) : ViewModel() {
}