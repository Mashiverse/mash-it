package com.mashiverse.mashit.data.models.artists

data class ArtistPage(
    val alias: String,
    val name: String,
    val bio: String,
    val bannerUrl: String,
    val mashup: ArtistMashup
)