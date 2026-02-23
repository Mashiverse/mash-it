package com.mashiverse.mashit.data.models.mashup.save

import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.UUID

@Serializable
data class SaveMashupReq(
    val walletAddress: String,
    val mashup: MashupData
)

@Serializable
data class MashupData(
    val id: String = "draft_${System.currentTimeMillis()}",
    val ts: Long = System.currentTimeMillis(),
    val createdAt: String = DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
    val colors: MashupColors,
    val layers: List<MashupLayer>
)

@Serializable
data class MashupColors(
    val base: String,
    val eyes: String,
    val hair: String
)

@Serializable
data class MashupLayer(
    val name: String,
    val image: String
)