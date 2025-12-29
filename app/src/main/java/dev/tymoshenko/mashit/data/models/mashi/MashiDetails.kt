package dev.tymoshenko.mashit.data.models.mashi

data class MashiDetails(
    val name: String,
    val author: String,
    val description: String,
    val compositeUrl: String,
    val traits: List<Trait>,
    val productInfo: ProductInfo? = null
)

data class ProductInfo(
    val price: Int,
    val perWallet: Int,
    val soldQuantity: Int,
    val quantity: Int,
    val priceCurrency: PriceCurrency = PriceCurrency.USDC,
)

enum class PriceCurrency {
    USDC,
    MATIC
}
