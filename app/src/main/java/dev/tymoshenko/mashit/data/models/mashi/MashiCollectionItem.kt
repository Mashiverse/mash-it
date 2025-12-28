package dev.tymoshenko.mashit.data.models.mashi

import dev.tymoshenko.mashit.data.models.color.TraitColor

data class MashiCollectionItem(
    val name: String,
    val author: String,
    val description: String,
    val compositeUrl: String,
    val traits: List<Trait>,
)
