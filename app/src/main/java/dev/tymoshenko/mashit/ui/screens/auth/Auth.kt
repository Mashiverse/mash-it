package dev.tymoshenko.mashit.ui.screens.auth

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import dev.tymoshenko.mashit.data.models.WalletPreferences

@Composable
fun Auth(navigateToApp: () -> Unit) {
    val ctx = LocalContext.current

    val viewModel = hiltViewModel<AuthViewModel>()
    val walletPreferences = viewModel.walletPreferences.collectAsState(
        WalletPreferences(null, null)
    )
    val wallet by remember(walletPreferences.value) {
        walletPreferences
    }

    if (wallet.wallet != null && wallet.walletType != null) {
        navigateToApp.invoke()
    }

    Button(onClick = { viewModel.connectMetamask() }) {
        Text("Connect MetaMask")
    }

    Button(onClick = {viewModel.connectCoinbase() }) {
        Text("Connect Coinbase")
    }
}
