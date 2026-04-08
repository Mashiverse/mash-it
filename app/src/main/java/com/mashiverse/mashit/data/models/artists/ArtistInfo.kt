package com.mashiverse.mashit.data.models.artists

import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors

data class ArtistInfo(
    val alias: String,
    val name: String,
    val mashup: ArtistMashup
)

