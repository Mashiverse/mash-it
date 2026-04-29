package com.mashiverse.mashit.data.remote.dtos

data class MetadataDto(
    val assets: List<Asset>,
    val attributes: List<Attribute>,
    val description: String?,
    val image: String,
    val name: String
) {
    data class Asset(
        val label: String,
        val type: String,
        val uri: String
    )

    data class Attribute(
        val trait_type: String?,
        val value: String?
    )
}

