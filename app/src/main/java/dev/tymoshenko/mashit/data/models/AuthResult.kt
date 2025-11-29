package dev.tymoshenko.mashit.data.models

data class AuthResult(
    val isSignedIn: Boolean,
    val errMessage: String?
)