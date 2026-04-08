package com.mashiverse.mashit.data.remote.dtos.artists

data class ArtistPageDto(
    val artist: Artist,
    val success: Boolean
) {
    data class Artist(
        val alias: String,
        val avatarUrl: Any,
        val bannerUrlDesktop: String,
        val bannerUrlMobile: String,
        val bio: String,
        val listingCount: Int,
        val mashupPfp: MashupPfp,
        val name: String,
        val wallet: String
    ) {
        data class MashupPfp(
            val colors: Colors,
            val layers: List<String>
        ) {
            data class Colors(
                val base: String,
                val eyes: String,
                val hair: String
            )
        }
    }
}