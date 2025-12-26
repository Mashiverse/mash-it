package dev.tymoshenko.mashit.data.models.auth

data class AuthResult(
    val isSignedIn: Boolean,
    val errMessage: String?
)