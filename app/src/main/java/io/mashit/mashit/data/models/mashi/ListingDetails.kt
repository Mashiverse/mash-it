package io.mashit.mashit.data.models.mashi

data class ListingDetails(
    val name: String,
    val author: String,
    val description: String?,
    val productInfo: ProductInfo,
    val compositeUrl: String,
    val isPaused: Boolean,
    val id: String,
    val listingId: String
)

data class ProductInfo(
    val price: Int,
    val perWallet: Int,
    val soldQuantity: Int,
    val quantity: Int,
    val priceCurrency: PriceCurrency = PriceCurrency.USDC
)

enum class PriceCurrency {
    USDC,
    POL
}

data class ShopListings(
    val listings: List<ListingDetails>,
    val hasMore: Boolean
)