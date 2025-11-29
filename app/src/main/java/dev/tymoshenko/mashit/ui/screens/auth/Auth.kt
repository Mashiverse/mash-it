package dev.tymoshenko.mashit.ui.screens.auth

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun Auth(navigateToApp: () -> Unit) {
    val ctx = LocalContext.current

    val viewModel = hiltViewModel<AuthViewModel>()
    val authRes by remember { viewModel.authRes }

//    if (authRes.isSignedIn) {
//        navigateToApp.invoke()
//    }
    Button(onClick = { viewModel.signInWithGoogle(ctx) }) {
        Text("Sign in with Google")
    }
}
