package com.mashiverse.mashit.data.remote.dtos

data class ListingsDto(
    val count: Int,
    val listings: List<Listings>,
    val pagination: Pagination,
    val success: Boolean,
    val timestamp: String
) {
    data class Listings(
        val artistName: String,
        val artistWallet: String,
        val chainId: Int,
        val createdAt: Any,
        val currency: String,
        val description: String,
        val id: String,
        val images: Images,
        val listingId: String?,
        val marketplace: String,
        val maxPerWallet: Int,
        val maxSupply: Int,
        val paused: Boolean,
        val price: String,
        val status: String,
        val title: String,
        val tokenURI: String,
        val totalSold: Int
    ) {
        data class Images(
            val composite: String,
            val thumbnail: String
        )
    }

    data class Pagination(
        val hasMore: Boolean,
        val limit: Int,
        val offset: Int,
        val total: Int
    )
}