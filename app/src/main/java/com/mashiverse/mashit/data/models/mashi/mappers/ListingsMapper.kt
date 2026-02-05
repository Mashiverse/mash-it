package com.mashiverse.mashit.data.models.mashi.mappers

import android.util.Log
import com.mashiverse.mashit.data.models.mashi.MashiTrait
import com.mashiverse.mashit.data.models.mashi.NftDetails
import com.mashiverse.mashit.data.models.mashi.PriceCurrency
import com.mashiverse.mashit.data.models.mashi.ProductInfo
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.data.remote.dtos.ListingDto
import com.mashiverse.mashit.data.remote.dtos.ListingsDto

fun ListingsDto.toListingDetails() = this.listings.map { listing ->
    NftDetails.ListingDetails(
        name = listing.title,
        author = listing.artistName,
        description = listing.description,
        productInfo = ProductInfo(
            price = listing.price.toInt(),
            perWallet = listing.maxPerWallet,
            soldQuantity = listing.totalSold,
            quantity = listing.maxSupply,
            priceCurrency = PriceCurrency.valueOf(listing.currency)
        ),
        isPaused = listing.paused,
        id = listing.id,
        listingId = listing.listingId,
        compositeUrl = listing.images.composite
    )
}

fun ListingDto.toListingDetails(): NftDetails.ListingDetails {
    val listing = this.listing
    val traits = listing.metadata.assets.filter { asset ->
        asset.label.lowercase() != "composite"
    }.map { asset ->
        MashiTrait(
            traitType = TraitType.valueOf(asset.label.uppercase()),
            url = asset.uri.replace("ipfs://", "https://ipfs.filebase.io/ipfs/")
        )
    }

    return NftDetails.ListingDetails(
        id = listing.id,
        name = listing.title,
        author = listing.artistName,
        description = listing.description ?: "",
        compositeUrl = listing.images.composite,
        mashiTraits = traits,
        isPaused = listing.paused,
        listingId = listing.listingId,
        productInfo = ProductInfo(
            price = listing.price.toInt(),
            perWallet = listing.maxPerWallet,
            soldQuantity = listing.totalSold,
            quantity = listing.maxSupply,
            priceCurrency = PriceCurrency.valueOf(listing.currency)
        )
    )
}
