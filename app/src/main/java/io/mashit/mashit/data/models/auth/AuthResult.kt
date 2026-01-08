package io.mashit.mashit.data.models.auth

data class AuthResult(
    val isSignedIn: Boolean,
    val errMessage: String?
)