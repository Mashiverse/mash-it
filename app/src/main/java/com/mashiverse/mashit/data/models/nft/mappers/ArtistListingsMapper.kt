package com.mashiverse.mashit.data.models.nft.mappers

import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.models.nft.PriceCurrency
import com.mashiverse.mashit.data.models.nft.ProductInfo
import com.mashiverse.mashit.data.remote.dtos.artists.ArtistListingsDto

fun ArtistListingsDto.toNfts() = this.listings
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