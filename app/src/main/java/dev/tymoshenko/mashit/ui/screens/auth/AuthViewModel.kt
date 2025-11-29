package dev.tymoshenko.mashit.ui.screens.auth

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tymoshenko.mashit.data.models.AuthResult
import dev.tymoshenko.mashit.utils.helpers.signIn
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val _authRes = mutableStateOf(
        AuthResult(
            isSignedIn = false,
            errMessage = null
        )
    )
    val authRes: State<AuthResult> = _authRes

    init {
        checkIfSignedIn()

    }

    private fun checkIfSignedIn() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            _authRes.value = AuthResult(
                isSignedIn = true,
                errMessage = null
            )
        }
    }

    fun signInWithGoogle(ctx: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            _authRes.value = signIn(
                auth = firebaseAuth,
                ctx = ctx,
            )
        }
    }
}