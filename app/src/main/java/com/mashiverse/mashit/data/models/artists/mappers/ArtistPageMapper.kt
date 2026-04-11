package com.mashiverse.mashit.data.models.artists.mappers

import com.mashiverse.mashit.data.models.artists.ArtistMashup
import com.mashiverse.mashit.data.models.artists.ArtistPageInfo
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.data.remote.dtos.artists.ArtistPageDto

fun ArtistPageDto.toArtistPage(): ArtistPageInfo {
    val pfp = artist.mashupPfp
    val pfpColors = pfp?.colors
    val mashupColors = if (pfpColors != null) {
        SelectedColors(
            base = pfpColors.base,
            eyes = pfpColors.eyes,
            hair = pfpColors.hair
        )
    } else SelectedColors()

    val mashup = ArtistMashup(
        colors = mashupColors,
        layers = pfp?.layers?.map { it.replace("/https://ipfs.io/ipfs", "") } ?: emptyList()
    )

    val alias = artist.alias
    val name = artist.name
    val bio = artist.bio
    val desktopBannerUrl = artist.bannerUrlDesktop
    val bannerUrl = artist.bannerUrlMobile

    return ArtistPageInfo(
        alias = alias,
        name = name,
        bio = bio ?: "",
        bannerUrl = bannerUrl ?: "",
        desktopBannerUrl = desktopBannerUrl ?: "",
        mashup = mashup
    )
}
