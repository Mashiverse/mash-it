package com.mashiverse.mashit.data.models.mashi

sealed class NftDetails(
    open val name: String,
    open val author: String,
    open val description: String?,
    open val productInfo: ProductInfo?,
    open val compositeUrl: String,
    open val mashiTraits: List<MashiTrait>? = emptyList()
) {
    data class ListingDetails(
        override val name: String,
        override val author: String,
        override val description: String?,
        override val productInfo: ProductInfo,
        override val compositeUrl: String,
        override val mashiTraits: List<MashiTrait>? = emptyList(),
        val isPaused: Boolean,
        val id: String,
        val listingId: String
    ) : NftDetails(
        name = name,
        author = author,
        description = description,
        productInfo = productInfo,
        compositeUrl = compositeUrl
    )

    data class MashiDetails(
        override val name: String,
        override val author: String,
        override val description: String,
        override val compositeUrl: String,
        override val mashiTraits: List<MashiTrait>,
        override val productInfo: ProductInfo? = null
    ) : NftDetails(
        name = name,
        author = author,
        description = description,
        productInfo = productInfo,
        compositeUrl = compositeUrl
    )
}

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
    val listings: List<NftDetails.ListingDetails>,
    val hasMore: Boolean
)