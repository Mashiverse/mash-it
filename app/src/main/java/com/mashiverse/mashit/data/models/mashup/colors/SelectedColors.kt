package com.mashiverse.mashit.data.models.mashup.colors

import kotlinx.serialization.Serializable

@Serializable
data class SelectedColors(
    val base: String,
    val eyes: String,
    val hair: String
)
