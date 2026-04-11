package com.mashiverse.mashit.data.remote.dtos.artists

data class ArtistsDto(
    val artists: List<Artist>,
    val count: Int,
    val pagination: Pagination,
    val success: Boolean,
    val timestamp: String
) {
    data class Artist(
        val alias: String,
        val avatarUrl: String,
        val bannerUrlDesktop: String,
        val bannerUrlMobile: String,
        val bio: String,
        val listingCount: Int,
        val mashupPfp: MashupPfp?,
        val name: String,
        val wallet: String
    ) {
        data class MashupPfp(
            val colors: Colors?,
            val layers: List<String>
        ) {
            data class Colors(
                val base: String,
                val eyes: String,
                val hair: String
            )
        }
    }

    data class Pagination(
        val hasMore: Boolean,
        val limit: Int,
        val offset: Int,
        val total: Int
    )
}