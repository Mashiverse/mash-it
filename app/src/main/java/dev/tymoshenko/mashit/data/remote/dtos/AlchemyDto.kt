package dev.tymoshenko.mashit.data.remote.dtos

data class AlchemyDto(
    val ownedNfts: List<OwnedNft>,
    val pageKey: String?,
    val totalCount: Int,
    val validAt: ValidAt
) {
    data class OwnedNft(
        val acquiredAt: AcquiredAt,
        val animation: Animation,
        val balance: String,
        val collection: Collection,
        val contract: Contract,
        val description: String,
        val image: Image,
        val mint: Mint,
        val name: String,
        val owners: Any,
        val raw: Raw,
        val timeLastUpdated: String,
        val tokenId: String,
        val tokenType: String,
        val tokenUri: String
    ) {
        data class AcquiredAt(
            val blockNumber: Any,
            val blockTimestamp: Any
        )

        data class Animation(
            val cachedUrl: Any,
            val contentType: Any,
            val originalUrl: Any,
            val size: Any
        )

        data class Collection(
            val bannerImageUrl: Any,
            val externalUrl: Any,
            val name: String,
            val slug: String
        )

        data class Contract(
            val address: String,
            val contractDeployer: String,
            val deployedBlockNumber: Int,
            val isSpam: Boolean,
            val name: String,
            val openSeaMetadata: OpenSeaMetadata,
            val spamClassifications: List<String>,
            val symbol: String,
            val tokenType: String,
            val totalSupply: Any
        ) {
            data class OpenSeaMetadata(
                val bannerImageUrl: Any,
                val collectionName: String,
                val collectionSlug: String,
                val description: Any,
                val discordUrl: Any,
                val externalUrl: Any,
                val floorPrice: Any,
                val imageUrl: String,
                val lastIngestedAt: String,
                val safelistRequestStatus: String,
                val twitterUsername: Any
            )
        }

        data class Image(
            val cachedUrl: String,
            val contentType: String,
            val originalUrl: String,
            val pngUrl: String,
            val size: Int,
            val thumbnailUrl: String
        )

        data class Mint(
            val blockNumber: Int,
            val mintAddress: String,
            val timestamp: String,
            val transactionHash: String
        )

        data class Raw(
            val error: Any,
            val metadata: Metadata,
            val tokenUri: String
        ) {
            data class Metadata(
                val assets: List<Asset>,
                val attributes: List<Attribute>,
                val description: String,
                val image: String,
                val name: String
            ) {
                data class Asset(
                    val label: String,
                    val type: String,
                    val uri: String
                )

                data class Attribute(
                    val trait_type: String,
                    val value: Any
                )
            }
        }
    }

    data class ValidAt(
        val blockHash: String,
        val blockNumber: Int,
        val blockTimestamp: String
    )
}