package com.mashiverse.mashit.data.models.mashi

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

val activeTraits = listOf(
    TraitType.BACKGROUND,
    TraitType.EYES,
    TraitType.BOTTOM,
    TraitType.UPPER,
    TraitType.HEAD
)


