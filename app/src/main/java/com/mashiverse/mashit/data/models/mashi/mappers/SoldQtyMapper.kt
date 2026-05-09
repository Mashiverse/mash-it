package com.mashiverse.mashit.data.models.mashi.mappers

import com.mashiverse.mashit.data.models.mashi.PriceCurrency
import com.mashiverse.mashit.data.models.mashi.ProductInfo
import com.mashiverse.mashit.data.remote.dtos.listings.SoldQtyDto

//fun SoldQtyDto.toProductInfoList() = this.listings.map {
//    ProductInfo(
//        price = it.price.toDouble(),
//        perWallet = it.maxPerWallet,
//        soldQuantity = it.totalSold,
//        quantity = it.maxSupply,
//        priceCurrency = PriceCurrency.valueOf(it.currency),
//        delisted = it.paused,
//        id = it.id,
//        listingId = it.listingId
//    )
//}