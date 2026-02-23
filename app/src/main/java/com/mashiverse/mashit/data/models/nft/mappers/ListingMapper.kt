package com.mashiverse.mashit.data.models.nft.mappers

import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.models.nft.PriceCurrency
import com.mashiverse.mashit.data.models.nft.ProductInfo
import com.mashiverse.mashit.data.models.nft.Trait
import com.mashiverse.mashit.data.models.nft.TraitType
import com.mashiverse.mashit.data.remote.dtos.ListingDto
import com.mashiverse.mashit.data.remote.dtos.ListingsDto

fun ListingsDto.toNfts() = this.listings
    .filter { it.listingId != null }
    .map { listing ->
        val productInfo = ProductInfo(
            price = listing.price.toDouble(),
            perWallet = listing.maxPerWallet,
            soldQuantity = listing.totalSold,
            quantity = listing.maxSupply,
            priceCurrency = PriceCurrency.valueOf(listing.currency),
            delisted = listing.status == "delisted",
            id = listing.id,
            listingId = listing.listingId!!,
        )

        Nft(
            name = listing.title,
            author = listing.artistName,
            description = listing.description,
            productInfo = productInfo,
            compositeUrl = listing.images.composite
        )
    }

fun ListingDto.toNft(): Nft {
    val listing = this.listing

    val traits = listing.metadata.assets.filter { asset ->
        asset.label.lowercase() != "composite"
    }.map { asset ->
        Trait(
            type = TraitType.valueOf(asset.label.uppercase()),
            url = asset.uri.replace("ipfs://", "https://ipfs.filebase.io/ipfs/")
        )
    }

    val productInfo = ProductInfo(
        price = listing.price.toDouble(),
        perWallet = listing.maxPerWallet,
        soldQuantity = listing.totalSold,
        quantity = listing.maxSupply,
        priceCurrency = PriceCurrency.valueOf(listing.currency),
        delisted = listing.status == "delisted",
        listingId = listing.listingId,
        id = listing.id
    )

    return Nft(
        name = listing.title,
        author = listing.artistName,
        description = listing.description ?: "",
        compositeUrl = listing.images.composite,
        traits = traits,
        productInfo = productInfo
    )
}
