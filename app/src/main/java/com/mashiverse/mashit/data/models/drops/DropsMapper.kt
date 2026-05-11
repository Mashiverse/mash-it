package com.mashiverse.mashit.data.models.drops

import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.PriceCurrency
import com.mashiverse.mashit.data.models.mashi.ProductInfo
import com.mashiverse.mashit.data.remote.dtos.drops.DropsDto
import com.mashiverse.mashit.data.remote.dtos.drops.SingleDropDto

fun DropsDto.toDrops() = this.drops.map { drop ->
    DropDetails(
        slug = drop.slug,
        imageUrl = drop.thumbnailUrl
    )
}

fun SingleDropDto.Drop.toNfts() = this.listings.map { listing ->
    val productInfo = ProductInfo(
        price = listing.price,
        perWallet = listing.maxPerWallet,
        soldQuantity = listing.totalSold,
        quantity = listing.maxSupply,
        priceCurrency = PriceCurrency.valueOf(listing.currency),
        delisted = listing.status == "delisted",
        id = listing.id,
        listingId = listing.listingId
    )

    Nft(
        name = listing.title,
        author = listing.artistName,
        description = listing.description,
        compositeUrl = listing.images.composite,
        productInfo = productInfo
    )
}

fun SingleDropDto.Drop.toInfo(): DropInfo {
    return DropInfo(
        name = this.name,
        description = this.description,
        desktopImageUrl = bannerUrlDesktop,
        mobileImageUrl = bannerUrlMobile
    )
}