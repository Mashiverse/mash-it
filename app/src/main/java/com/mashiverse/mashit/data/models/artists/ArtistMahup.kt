package com.mashiverse.mashit.data.models.artists

import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors

data class ArtistMashup(
    val colors: SelectedColors,
    val layers: List<String>
)