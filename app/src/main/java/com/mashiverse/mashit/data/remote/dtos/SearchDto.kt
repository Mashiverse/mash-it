package com.mashiverse.mashit.data.remote.dtos

data class SearchDto(
    val count: Int,
    val listings: List<Listings>,
    val pagination: Pagination,
    val query: String,
    val success: Boolean
) {
    data class Listings(
        val artistName: String,
        val artistWallet: String,
        val chainId: Int,
        val currency: String,
        val description: String,
        val id: String,
        val images: Images,
        val listingId: String?,
        val marketplace: String,
        val maxPerWallet: Int,
        val maxSupply: Int,
        val mintable: Boolean,
        val paused: Boolean,
        val price: String,
        val remaining: Int,
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