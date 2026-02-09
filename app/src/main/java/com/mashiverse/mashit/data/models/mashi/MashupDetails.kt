package com.mashiverse.mashit.data.models.mashi

import com.mashiverse.mashit.data.models.color.SelectedColors

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
