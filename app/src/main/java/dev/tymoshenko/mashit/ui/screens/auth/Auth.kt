package dev.tymoshenko.mashit.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.tymoshenko.mashit.data.models.wallet.WalletPreferences
import dev.tymoshenko.mashit.ui.theme.Background

@Composable
fun Auth(onConnect: () -> Unit) {
    val viewModel = hiltViewModel<AuthViewModel>()
    val walletPreferences = viewModel.walletPreferences.collectAsState(
        WalletPreferences(null, null)
    )
    val wallet by remember(walletPreferences.value) {
        walletPreferences
    }

    if (wallet.wallet != null && wallet.walletType != null) {
        onConnect.invoke()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column {
            Button(onClick = onConnect) {
                Text("Connect MetaMask")
            }

            Button(onClick = onConnect) {
                Text("Connect Coinbase")
            }
        }
    }
}
