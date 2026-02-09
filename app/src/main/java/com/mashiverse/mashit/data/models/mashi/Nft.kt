package com.mashiverse.mashit.data.models.mashi

data class Nft(
    val name: String,
    val author: String,
    val description: String?,
    val compositeUrl: String,
    val traits: List<Trait>? = emptyList(),
    val productInfo: ProductInfo? = null,
    val owned: List<Owned>? = null
)

data class ProductInfo(
    val price: Int,
    val perWallet: Int,
    val soldQuantity: Int,
    val quantity: Int,
    val priceCurrency: PriceCurrency = PriceCurrency.USDC,
    val isPaused: Boolean,
    val id: String,
    val listingId: String
)

enum class PriceCurrency {
    USDC,
    POL
}

data class ShopListings(
    val listings: List<Nft>,
    val hasMore: Boolean
)