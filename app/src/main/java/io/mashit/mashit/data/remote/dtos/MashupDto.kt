package io.mashit.mashit.data.remote.dtos

data class MashupDto(
    val assets: List<Asset>,
    val colors: Colors,
    val count: Int,
    val id: String,
    val timestamp: Long,
    val wallet: String
) {
    data class Asset(
        val image: String,
        val name: String
    )

    data class Colors(
        val base: String,
        val eyes: String,
        val hair: String
    )
}