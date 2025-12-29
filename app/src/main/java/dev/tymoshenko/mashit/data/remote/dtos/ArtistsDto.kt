package dev.tymoshenko.mashit.data.remote.dtos

data class ArtistsDto(
    val artists: List<Artist>,
    val count: Int,
    val success: Boolean
) {
    data class Artist(
        val alias: String,
        val listingCount: Int,
        val name: String,
        val profileImage: Any,
        val wallet: String
    )
}