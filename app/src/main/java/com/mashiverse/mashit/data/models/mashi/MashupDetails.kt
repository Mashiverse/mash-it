package com.mashiverse.mashit.data.models.mashi

import com.mashiverse.mashit.data.models.color.SelectedColors

data class MashupDetails(
    val assets: List<MashiTrait>? = null,
    val colors: SelectedColors = SelectedColors(
        base = "#00FF00",
        eyes = "#FFFF00",
        hair = "#0000FF"
    )
)
