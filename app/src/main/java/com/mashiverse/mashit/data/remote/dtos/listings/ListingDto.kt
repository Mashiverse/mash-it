package com.mashiverse.mashit.data.remote.dtos.listings

import com.google.gson.annotations.SerializedName

@Suppress("PropertyName")
data class ListingDto(
    @SerializedName("listing") val listing: Listing,
    @SerializedName("success") val success: Boolean
) {
    data class Listing(
        @SerializedName("artistName") val artistName: String,
        @SerializedName("artistWallet") val artistWallet: String,
        @SerializedName("chainId") val chainId: Int,
        @SerializedName("createdAt") val createdAt: Any, // Consider changing Any to String or Long
        @SerializedName("currency") val currency: String,
        @SerializedName("description") val description: String?,
        @SerializedName("id") val id: String,
        @SerializedName("images") val images: Images,
        @SerializedName("listingId") val listingId: String,
        @SerializedName("marketplace") val marketplace: String,
        @SerializedName("maxPerWallet") val maxPerWallet: Int,
        @SerializedName("maxSupply") val maxSupply: Int,
        @SerializedName("metadata") val metadata: Metadata,
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

        data class Metadata(
            @SerializedName("assets") val assets: List<Asset>,
            @SerializedName("attributes") val attributes: List<Attribute>,
            @SerializedName("description") val description: String,
            @SerializedName("image") val image: String,
            @SerializedName("name") val name: String
        ) {
            data class Asset(
                @SerializedName("label") val label: String,
                @SerializedName("uri") val uri: String
            )

            data class Attribute(
                @SerializedName("trait_type") val trait_type: String,
                @SerializedName("value") val value: String
            )
        }
    }
}