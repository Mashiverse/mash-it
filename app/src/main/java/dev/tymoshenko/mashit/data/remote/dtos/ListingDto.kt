package dev.tymoshenko.mashit.data.remote.dtos

data class ListingDto(
    val listing: Listing,
    val success: Boolean
) {
    data class Listing(
        val artistName: String,
        val artistWallet: String,
        val chainId: Int,
        val createdAt: Any,
        val currency: String,
        val description: String,
        val id: String,
        val images: Images,
        val listingId: String,
        val marketplace: String,
        val maxPerWallet: Int,
        val maxSupply: Int,
        val metadata: Metadata,
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

        data class Metadata(
            val assets: List<Asset>,
            val attributes: List<Attribute>,
            val description: String,
            val image: String,
            val name: String
        ) {
            data class Asset(
                val label: String,
                val uri: String
            )

            data class Attribute(
                val trait_type: String,
                val value: String
            )
        }
    }
}