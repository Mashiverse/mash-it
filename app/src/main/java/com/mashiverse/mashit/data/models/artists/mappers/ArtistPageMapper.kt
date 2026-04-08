package com.mashiverse.mashit.data.models.artists.mappers

import com.mashiverse.mashit.data.models.artists.ArtistMashup
import com.mashiverse.mashit.data.models.artists.ArtistPage
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.data.remote.dtos.artists.ArtistPageDto

fun ArtistPageDto.toArtistPage(): ArtistPage {
    val pfp = artist.mashupPfp
    val pfpColors = pfp.colors
    val mashupColors = SelectedColors(
        base = pfpColors.base,
        eyes = pfpColors.eyes,
        hair = pfpColors.hair
    )

    val mashup = ArtistMashup(
        colors = mashupColors,
        layers = pfp.layers
    )

    val alias = artist.alias
    val name = artist.name
    val bio = artist.bio
    val bannerUrl = artist.bannerUrlMobile

    return ArtistPage(
        alias = alias,
        name = name,
        bio = bio,
        bannerUrl = bannerUrl,
        mashup = mashup
    )
}
