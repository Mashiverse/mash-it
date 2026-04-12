package com.mashiverse.mashit.data.remote.dtos.listings

import com.google.gson.annotations.SerializedName

data class ListingsDto(
    @SerializedName("count") val count: Int,
    @SerializedName("listings") val listings: List<Listings>,
    @SerializedName("pagination") val pagination: Pagination,
    @SerializedName("success") val success: Boolean,
    @SerializedName("timestamp") val timestamp: String
) {
    data class Listings(
        @SerializedName("artistName") val artistName: String,
        @SerializedName("artistWallet") val artistWallet: String,
        @SerializedName("chainId") val chainId: Int,
        @SerializedName("createdAt") val createdAt: Any,
        @SerializedName("currency") val currency: String,
        @SerializedName("description") val description: String,
        @SerializedName("id") val id: String,
        @SerializedName("images") val images: Images,
        @SerializedName("listingId") val listingId: String?,
        @SerializedName("marketplace") val marketplace: String,
        @SerializedName("maxPerWallet") val maxPerWallet: Int,
        @SerializedName("maxSupply") val maxSupply: Int,
        @SerializedName("paused") val paused: Boolean,
        @SerializedName("price") val price: String,
        @SerializedName("status") val status: String,
        @SerializedName("title") val title: String,
        @SerializedName("tokenURI") val tokenURI: String,
        @SerializedName("totalSold") val totalSold: Int
    ) {
        data class Images(
            @SerializedName("composite") val composite: String,
            @SerializedName("thumbnail") val thumbnail: String
        )
    }

    data class Pagination(
        @SerializedName("hasMore") val hasMore: Boolean,
        @SerializedName("limit") val limit: Int,
        @SerializedName("offset") val offset: Int,
        @SerializedName("total") val total: Int
    )
}