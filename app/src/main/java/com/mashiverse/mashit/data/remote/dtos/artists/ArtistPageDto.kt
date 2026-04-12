package com.mashiverse.mashit.data.remote.dtos.artists

import com.google.gson.annotations.SerializedName

data class ArtistPageDto(
    @SerializedName("artist") val artist: Artist,
    @SerializedName("success") val success: Boolean
) {
    data class Artist(
        @SerializedName("alias") val alias: String,
        @SerializedName("avatarUrl") val avatarUrl: Any, // Consider String? if possible
        @SerializedName("bannerUrlDesktop") val bannerUrlDesktop: String?,
        @SerializedName("bannerUrlMobile") val bannerUrlMobile: String?,
        @SerializedName("bio") val bio: String?,
        @SerializedName("listingCount") val listingCount: Int,
        @SerializedName("mashupPfp") val mashupPfp: MashupPfp?,
        @SerializedName("name") val name: String,
        @SerializedName("wallet") val wallet: String
    ) {
        data class MashupPfp(
            @SerializedName("colors") val colors: Colors,
            @SerializedName("layers") val layers: List<String>
        ) {
            data class Colors(
                @SerializedName("base") val base: String,
                @SerializedName("eyes") val eyes: String,
                @SerializedName("hair") val hair: String
            )
        }
    }
}