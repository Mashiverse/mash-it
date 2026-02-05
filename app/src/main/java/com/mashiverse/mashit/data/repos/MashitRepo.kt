package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.models.color.SelectedColors
import com.mashiverse.mashit.data.models.mashi.MashupDetails
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.ShopListings
import com.mashiverse.mashit.data.models.mashi.Trait
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.data.models.mashi.mappers.toNftDetails
import com.mashiverse.mashit.data.models.mashi.mappers.toNftsDetails
import com.mashiverse.mashit.data.remote.apis.MashitApi
import javax.inject.Inject

class MashitRepo @Inject constructor(private val mashitApi: MashitApi) {

    suspend fun getShopList(
        limit: Int = 20,
        offset: Int = 0
    ): ShopListings {
        val listingsDto = mashitApi.getShopList(limit = limit, offset = offset)
        val hasMore = listingsDto.pagination.hasMore
        val listings = listingsDto.toNftsDetails()

        return ShopListings(
            listings = listings,
            hasMore = hasMore
        )
    }

    suspend fun getShopItem(
        id: String
    ): Nft {
        val listingDto = mashitApi.getShopItem(id)
        return listingDto.toNftDetails()
    }

    suspend fun getMashup(
        wallet: String
    ): MashupDetails {
        val mashupDto = mashitApi.getMashup(wallet)
        val traits = mashupDto.assets.map { asset ->
            Trait(
                type = TraitType.valueOf(asset.name.uppercase()),
                url = asset.image.replace("https://ipfs.", "https://ipfs.filebase.")
            )
        }

        val colors = SelectedColors(
            base = mashupDto.colors.base,
            eyes = mashupDto.colors.eyes,
            hair = mashupDto.colors.hair
        )

        return MashupDetails(
            assets = traits,
            colors = colors
        )
    }
}