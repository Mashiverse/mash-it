package com.mashiverse.mashit.data.models.nft

import kotlinx.serialization.Serializable

@Serializable
data class Trait(
    val type: TraitType,
    val url: String? = null
)

@Serializable
enum class TraitType {
    BACKGROUND,
    HAIR_BACK,
    CAPE,
    BOTTOM,
    UPPER,
    HEAD,
    EYES,
    HAIR_FRONT,
    HAT,
    LEFT_ACCESSORY,
    RIGHT_ACCESSORY
}


