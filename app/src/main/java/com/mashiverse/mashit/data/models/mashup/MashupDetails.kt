package com.mashiverse.mashit.data.models.mashup

import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.data.models.nft.Trait
import com.mashiverse.mashit.data.models.nft.TraitType

data class MashupDetails(
    val assets: List<Trait> = List(11) { i ->
        Trait(
            url = null,
            type = TraitType.entries[i]
        )
    },
    val colors: SelectedColors = SelectedColors(
        base = "#00FF00",
        eyes = "#FFFF00",
        hair = "#0000FF"
    ),
    val name: String? = null,
    val mint: Int? = null
)
