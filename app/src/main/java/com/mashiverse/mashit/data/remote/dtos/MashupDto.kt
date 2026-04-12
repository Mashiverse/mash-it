package com.mashiverse.mashit.data.remote.dtos

import com.google.gson.annotations.SerializedName

data class MashupDto(
    @SerializedName("assets") val assets: List<Asset>,
    @SerializedName("colors") val colors: Colors,
    @SerializedName("count") val count: Int,
    @SerializedName("id") val id: String,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("wallet") val wallet: String
) {
    data class Asset(
        @SerializedName("image") val image: String,
        @SerializedName("name") val name: String
    )

    data class Colors(
        @SerializedName("base") val base: String,
        @SerializedName("eyes") val eyes: String,
        @SerializedName("hair") val hair: String
    )
}