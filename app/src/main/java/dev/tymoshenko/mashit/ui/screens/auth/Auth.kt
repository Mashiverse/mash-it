package dev.tymoshenko.mashit.ui.screens.auth

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Auth(navigateToApp: () -> Unit) {
    Button(onClick = navigateToApp) {
        Text("Sign In")
    }
}