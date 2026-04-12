@file:Suppress("PropertyName")

package com.mashiverse.mashit.data.remote.dtos

import com.google.gson.annotations.SerializedName

@Suppress("PropertyName")
data class AlchemyDto(
    @SerializedName("ownedNfts") val ownedNfts: List<OwnedNft>,
    @SerializedName("pageKey") val pageKey: String?,
    @SerializedName("totalCount") val totalCount: Int,
    @SerializedName("validAt") val validAt: ValidAt
) {
    data class OwnedNft(
        @SerializedName("acquiredAt") val acquiredAt: AcquiredAt,
        @SerializedName("animation") val animation: Animation,
        @SerializedName("balance") val balance: String,
        @SerializedName("collection") val collection: Collection,
        @SerializedName("contract") val contract: Contract,
        @SerializedName("description") val description: String,
        @SerializedName("image") val image: Image,
        @SerializedName("mint") val mint: Mint,
        @SerializedName("name") val name: String,
        @SerializedName("owners") val owners: Any,
        @SerializedName("raw") val raw: Raw,
        @SerializedName("timeLastUpdated") val timeLastUpdated: String,
        @SerializedName("tokenId") val tokenId: String,
        @SerializedName("tokenType") val tokenType: String,
        @SerializedName("tokenUri") val tokenUri: String
    ) {
        data class AcquiredAt(
            @SerializedName("blockNumber") val blockNumber: Any,
            @SerializedName("blockTimestamp") val blockTimestamp: Any
        )

        data class Animation(
            @SerializedName("cachedUrl") val cachedUrl: Any,
            @SerializedName("contentType") val contentType: Any,
            @SerializedName("originalUrl") val originalUrl: Any,
            @SerializedName("size") val size: Any
        )

        data class Collection(
            @SerializedName("bannerImageUrl") val bannerImageUrl: Any,
            @SerializedName("externalUrl") val externalUrl: Any,
            @SerializedName("name") val name: String,
            @SerializedName("slug") val slug: String
        )

        data class Contract(
            @SerializedName("address") val address: String,
            @SerializedName("contractDeployer") val contractDeployer: String,
            @SerializedName("deployedBlockNumber") val deployedBlockNumber: Int,
            @SerializedName("isSpam") val isSpam: Boolean,
            @SerializedName("name") val name: String,
            @SerializedName("openSeaMetadata") val openSeaMetadata: OpenSeaMetadata,
            @SerializedName("spamClassifications") val spamClassifications: List<String>,
            @SerializedName("symbol") val symbol: String,
            @SerializedName("tokenType") val tokenType: String,
            @SerializedName("totalSupply") val totalSupply: Any
        ) {
            data class OpenSeaMetadata(
                @SerializedName("bannerImageUrl") val bannerImageUrl: Any,
                @SerializedName("collectionName") val collectionName: String,
                @SerializedName("collectionSlug") val collectionSlug: String,
                @SerializedName("description") val description: Any,
                @SerializedName("discordUrl") val discordUrl: Any,
                @SerializedName("externalUrl") val externalUrl: Any,
                @SerializedName("floorPrice") val floorPrice: Any,
                @SerializedName("imageUrl") val imageUrl: String,
                @SerializedName("lastIngestedAt") val lastIngestedAt: String,
                @SerializedName("safelistRequestStatus") val safelistRequestStatus: String,
                @SerializedName("twitterUsername") val twitterUsername: Any
            )
        }

        data class Image(
            @SerializedName("cachedUrl") val cachedUrl: String,
            @SerializedName("contentType") val contentType: String,
            @SerializedName("originalUrl") val originalUrl: String,
            @SerializedName("pngUrl") val pngUrl: String,
            @SerializedName("size") val size: Int,
            @SerializedName("thumbnailUrl") val thumbnailUrl: String
        )

        data class Mint(
            @SerializedName("blockNumber") val blockNumber: Int,
            @SerializedName("mintAddress") val mintAddress: String,
            @SerializedName("timestamp") val timestamp: String,
            @SerializedName("transactionHash") val transactionHash: String
        )

        data class Raw(
            @SerializedName("error") val error: Any,
            @SerializedName("metadata") val metadata: Metadata,
            @SerializedName("tokenUri") val tokenUri: String
        ) {
            data class Metadata(
                @SerializedName("assets") val assets: List<Asset>,
                @SerializedName("attributes") val attributes: List<Attribute>,
                @SerializedName("description") val description: String,
                @SerializedName("image") val image: String,
                @SerializedName("name") val name: String
            ) {
                data class Asset(
                    @SerializedName("label") val label: String,
                    @SerializedName("type") val type: String,
                    @SerializedName("uri") val uri: String
                )

                data class Attribute(
                    @SerializedName("trait_type") val trait_type: String,
                    @SerializedName("value") val value: Any
                )
            }
        }
    }

    data class ValidAt(
        @SerializedName("blockHash") val blockHash: String,
        @SerializedName("blockNumber") val blockNumber: Int,
        @SerializedName("blockTimestamp") val blockTimestamp: String
    )
}