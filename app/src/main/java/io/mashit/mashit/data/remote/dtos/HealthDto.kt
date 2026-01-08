package io.mashit.mashit.data.remote.dtos

data class HealthDto(
    val status: String,
    val success: Boolean,
    val timestamp: String,
    val version: String
)