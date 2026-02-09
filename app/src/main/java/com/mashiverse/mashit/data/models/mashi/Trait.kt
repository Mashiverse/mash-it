package com.mashiverse.mashit.data.models.mashi

data class Trait(
    val type: TraitType,
    val url: String? = null
)

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


