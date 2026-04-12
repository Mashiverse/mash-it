package com.mashiverse.mashit.data.remote.dtos.artists

import com.google.gson.annotations.SerializedName

data class ArtistsDto(
    @SerializedName("artists") val artists: List<Artist>,
    @SerializedName("count") val count: Int,
    @SerializedName("pagination") val pagination: Pagination,
    @SerializedName("success") val success: Boolean,
    @SerializedName("timestamp") val timestamp: String
) {
    data class Artist(
        @SerializedName("alias") val alias: String,
        @SerializedName("avatarUrl") val avatarUrl: String,
        @SerializedName("bannerUrlDesktop") val bannerUrlDesktop: String,
        @SerializedName("bannerUrlMobile") val bannerUrlMobile: String,
        @SerializedName("bio") val bio: String,
        @SerializedName("listingCount") val listingCount: Int,
        @SerializedName("mashupPfp") val mashupPfp: MashupPfp?,
        @SerializedName("name") val name: String,
        @SerializedName("wallet") val wallet: String
    ) {
        data class MashupPfp(
            @SerializedName("colors") val colors: Colors?,
            @SerializedName("layers") val layers: List<String>
        ) {
            data class Colors(
                @SerializedName("base") val base: String,
                @SerializedName("eyes") val eyes: String,
                @SerializedName("hair") val hair: String
            )
        }
    }

    data class Pagination(
        @SerializedName("hasMore") val hasMore: Boolean,
        @SerializedName("limit") val limit: Int,
        @SerializedName("offset") val offset: Int,
        @SerializedName("total") val total: Int
    )
}