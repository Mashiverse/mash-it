package com.mashiverse.mashit.data.remote.dtos.drops

import com.google.gson.annotations.SerializedName

data class SingleDropDto(
    @SerializedName("drop")
    val drop: Drop,
    @SerializedName("success")
    val success: Boolean
) {
    data class Drop(
        @SerializedName("bannerUrlDesktop")
        val bannerUrlDesktop: String,
        @SerializedName("bannerUrlMobile")
        val bannerUrlMobile: String,
        @SerializedName("description")
        val description: String?,
        @SerializedName("id")
        val id: String,
        @SerializedName("listings")
        val listings: List<Listing>,
        @SerializedName("name")
        val name: String,
        @SerializedName("nftCount")
        val nftCount: Int,
        @SerializedName("slug")
        val slug: String,
        @SerializedName("thumbnailUrl")
        val thumbnailUrl: String
    ) {
        data class Listing(
            @SerializedName("artistName")
            val artistName: String,
            @SerializedName("artistWallet")
            val artistWallet: String,
            @SerializedName("chainId")
            val chainId: Int,
            @SerializedName("currency")
            val currency: String,
            @SerializedName("description")
            val description: String?,
            @SerializedName("id")
            val id: String,
            @SerializedName("images")
            val images: Images,
            @SerializedName("isSoldOut")
            val isSoldOut: Boolean,
            @SerializedName("listingId")
            val listingId: String,
            @SerializedName("marketplace")
            val marketplace: String,
            @SerializedName("maxPerWallet")
            val maxPerWallet: Int,
            @SerializedName("maxSupply")
            val maxSupply: Int,
            @SerializedName("mintable")
            val mintable: Boolean,
            @SerializedName("paused")
            val paused: Boolean,
            @SerializedName("price")
            val price: Double,
            @SerializedName("remaining")
            val remaining: Int,
            @SerializedName("status")
            val status: String,
            @SerializedName("title")
            val title: String,
            @SerializedName("tokenURI")
            val tokenURI: String,
            @SerializedName("totalSold")
            val totalSold: Int
        ) {
            data class Images(
                @SerializedName("composite")
                val composite: String,
                @SerializedName("thumbnail")
                val thumbnail: String
            )
        }
    }
}