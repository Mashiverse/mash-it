package com.mashiverse.mashit.data.remote.dtos.listings

data class SoldQtyDto(
    val count: Int,
    val listings: List<Listings>,
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
        val isSoldOut: Boolean,
        val listingId: String,
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
}