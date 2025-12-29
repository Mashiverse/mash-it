package dev.tymoshenko.mashit.data.models.mashi.mappers

import android.util.Log
import dev.tymoshenko.mashit.data.models.mashi.ListingDetails
import dev.tymoshenko.mashit.data.models.mashi.MashiDetails
import dev.tymoshenko.mashit.data.models.mashi.MashiTrait
import dev.tymoshenko.mashit.data.models.mashi.PriceCurrency
import dev.tymoshenko.mashit.data.models.mashi.ProductInfo
import dev.tymoshenko.mashit.data.models.mashi.TraitType
import dev.tymoshenko.mashit.data.remote.dtos.ListingDto
import dev.tymoshenko.mashit.data.remote.dtos.ListingsDto

fun ListingsDto.toListingDetails() = this.listings.map { listing ->
    ListingDetails(
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

fun ListingDto.toMashiDetails(): MashiDetails {
    val listing = this.listing
    val traits = listing.metadata.assets.filter { asset ->
        asset.label.lowercase() != "composite"
    }.map { asset ->
        MashiTrait(
            traitType = TraitType.valueOf(asset.label.uppercase()),
            url = asset.uri.replace("ipfs://", "https://ipfs.filebase.io/ipfs/")
        )
    }

   Log.d("GG", traits.size.toString())

    return MashiDetails(
        name = listing.title,
        author = listing.artistName,
        description = listing.description,
        compositeUrl = listing.images.composite,
        mashiTraits = traits,
        productInfo = ProductInfo(
            price = listing.price.toInt(),
            perWallet = listing.maxPerWallet,
            soldQuantity = listing.totalSold,
            quantity = listing.maxSupply,
            priceCurrency = PriceCurrency.valueOf(listing.currency)
        )
    )
}
