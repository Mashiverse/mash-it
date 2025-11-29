package dev.tymoshenko.mashit.utils.helpers

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dev.tymoshenko.mashit.BuildConfig
import dev.tymoshenko.mashit.data.models.AuthResult
import kotlinx.coroutines.tasks.await

val googleIdOption by lazy {
    val clientId = BuildConfig.WEB_CLIENT_ID

    GetGoogleIdOption.Builder()
        .setServerClientId(clientId)
        .setFilterByAuthorizedAccounts(false)
        .setAutoSelectEnabled(false)
        .build()
}

val req by lazy {
    GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()
}

suspend fun signIn(auth: FirebaseAuth, ctx: Context): AuthResult {
    try {
        val credManager = CredentialManager.create(ctx)
        val res = credManager.getCredential(ctx, req)
        val cred = res.credential
        return handleSignIn(cred, auth)
    } catch (e: Exception) {
        return AuthResult(
            isSignedIn = false,
            errMessage = e.message
        )
    }
}

suspend fun handleSignIn(
    credential: Credential,
    auth: FirebaseAuth
): AuthResult {
    try {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            return firebaseAuthWithGoogle(googleIdTokenCredential.idToken, auth)
        }
        return AuthResult(
            isSignedIn = false,
            errMessage = "Something went wrong"
        )
    } catch (e: Exception) {
        return AuthResult(
            isSignedIn = false,
            errMessage = e.message
        )
    }
}

suspend fun firebaseAuthWithGoogle(
    idToken: String,
    auth: FirebaseAuth,
): AuthResult {
    try {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        var result: AuthResult? = null
        (auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                result = if (task.isSuccessful) {
                    AuthResult(
                        isSignedIn = true,
                        errMessage = null
                    )
                } else {
                    AuthResult(
                        isSignedIn = false,
                        errMessage = task.exception?.message
                    )
                }
            }).await()

        return result ?: AuthResult(
            isSignedIn = false,
            errMessage = "Something went wrong"
        )
    } catch (e: Exception) {
        return AuthResult(
            isSignedIn = false,
            errMessage = e.message
        )
    }
}

suspend fun signOut(auth: FirebaseAuth, ctx: Context) {
    auth.signOut()
    val credManager = CredentialManager.create(ctx)
    credManager.clearCredentialState(ClearCredentialStateRequest())
}