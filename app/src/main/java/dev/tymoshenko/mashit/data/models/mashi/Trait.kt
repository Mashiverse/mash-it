package dev.tymoshenko.mashit.data.models.mashi

data class Trait(
    val traitType: TraitType,
    val url: String
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


