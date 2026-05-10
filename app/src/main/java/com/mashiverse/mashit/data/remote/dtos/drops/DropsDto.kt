package com.mashiverse.mashit.data.remote.dtos.drops

import com.google.gson.annotations.SerializedName

data class DropsDto(
    @SerializedName("count")
    val count: Int,
    @SerializedName("drops")
    val drops: List<Drop>,
    @SerializedName("success")
    val success: Boolean
) {
    data class Drop(
        @SerializedName("createdAt")
        val createdAt: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("landingUrl")
        val landingUrl: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("nftCount")
        val nftCount: Int,
        @SerializedName("slug")
        val slug: String,
        @SerializedName("thumbnailUrl")
        val thumbnailUrl: String
    )
}