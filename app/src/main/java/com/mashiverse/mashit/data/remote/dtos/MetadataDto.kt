package com.mashiverse.mashit.data.remote.dtos

import com.google.gson.annotations.SerializedName

data class MetadataDto(
    @SerializedName("assets")
    val assets: List<Asset>,
    @SerializedName("attributes")
    val attributes: List<Attribute>,
    @SerializedName("description")
    val description: String?,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String
) {
    data class Asset(
        @SerializedName("label")
        val label: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("uri")
        val uri: String
    )

    data class Attribute(
        @SerializedName("trait_type")
        val traitType: String?,
        @SerializedName("value")
        val value: String?
    )
}