package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.models.color.SelectedColors
import com.mashiverse.mashit.data.models.mashi.MashiDetails
import com.mashiverse.mashit.data.models.mashi.MashiTrait
import com.mashiverse.mashit.data.models.mashi.MashupDetails
import com.mashiverse.mashit.data.models.mashi.ShopListings
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.data.models.mashi.mappers.toListingDetails
import com.mashiverse.mashit.data.models.mashi.mappers.toMashiDetails
import com.mashiverse.mashit.data.remote.apis.MashItApi
import javax.inject.Inject

class MashItRepo @Inject constructor(private val mashItApi: MashItApi) {

    suspend fun getShopList(
        limit: Int = 20,
        offset: Int = 0
    ): ShopListings {
        val listingsDto = mashItApi.getShopList(limit = limit, offset = offset)
        val hasMore = listingsDto.pagination.hasMore
        val listings = listingsDto.toListingDetails()

        return ShopListings(
            listings = listings,
            hasMore = hasMore
        )
    }

    suspend fun getShopItem(
        id: String
    ): MashiDetails {
        val listingDto = mashItApi.getShopItem(id)
        return listingDto.toMashiDetails()
    }

    suspend fun getMashup(
        wallet: String
    ): MashupDetails {
        val mashupDto = mashItApi.getMashup(wallet)
        val traits = mashupDto.assets.map { asset ->
            MashiTrait(
                traitType = TraitType.valueOf(asset.name.uppercase()),
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