package com.mashiverse.mashit.data.models.mashup

import com.mashiverse.mashit.data.models.nft.Trait

data class MashupTrait(
    val trait: Trait,
    val avatarName: String,
    val mint: Int? = null
)