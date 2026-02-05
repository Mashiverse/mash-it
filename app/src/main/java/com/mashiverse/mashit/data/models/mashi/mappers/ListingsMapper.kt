package com.mashiverse.mashit.data.models.mashi.mappers

import com.mashiverse.mashit.data.models.mashi.NftDetails
import com.mashiverse.mashit.data.models.mashi.PriceCurrency
import com.mashiverse.mashit.data.models.mashi.ProductInfo
import com.mashiverse.mashit.data.models.mashi.Trait
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.data.remote.dtos.ListingDto
import com.mashiverse.mashit.data.remote.dtos.ListingsDto

fun ListingsDto.toNftsDetails() = this.listings.map { listing ->
    val productInfo = ProductInfo(
        price = listing.price.toInt(),
        perWallet = listing.maxPerWallet,
        soldQuantity = listing.totalSold,
        quantity = listing.maxSupply,
        priceCurrency = PriceCurrency.valueOf(listing.currency),
        isPaused = listing.paused,
        id = listing.id,
        listingId = listing.listingId,
    )

    NftDetails(
        name = listing.title,
        author = listing.artistName,
        description = listing.description,
        productInfo = productInfo,
        compositeUrl = listing.images.composite
    )
}

fun ListingDto.toNftDetails(): NftDetails {
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
        price = listing.price.toInt(),
        perWallet = listing.maxPerWallet,
        soldQuantity = listing.totalSold,
        quantity = listing.maxSupply,
        priceCurrency = PriceCurrency.valueOf(listing.currency),
        isPaused = listing.paused,
        listingId = listing.listingId,
        id = listing.id
    )

    return NftDetails(
        name = listing.title,
        author = listing.artistName,
        description = listing.description ?: "",
        compositeUrl = listing.images.composite,
        traits = traits,
        productInfo = productInfo
    )
}
