package com.mashiverse.mashit.data.models.artists

data class ArtistPageInfo(
    val alias: String,
    val name: String,
    val bio: String,
    val bannerUrl: String,
    val desktopBannerUrl: String,
    val mashup: ArtistMashup
)