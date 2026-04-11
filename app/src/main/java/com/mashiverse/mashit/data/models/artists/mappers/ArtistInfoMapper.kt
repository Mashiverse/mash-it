package com.mashiverse.mashit.data.models.artists.mappers

import com.mashiverse.mashit.data.models.artists.ArtistInfo
import com.mashiverse.mashit.data.models.artists.ArtistMashup
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.data.remote.dtos.artists.ArtistsDto

fun ArtistsDto.toArtists(): List<ArtistInfo> {
    val artists = this.artists

    return artists.map { artist ->
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

        ArtistInfo(
            alias = alias,
            name = name,
            mashup = mashup
        )
    }
}