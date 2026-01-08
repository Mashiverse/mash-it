package io.mashit.mashit.data.repos

import io.mashit.mashit.data.models.mashi.MashiDetails
import io.mashit.mashit.data.models.mashi.ShopListings
import io.mashit.mashit.data.models.mashi.mappers.toListingDetails
import io.mashit.mashit.data.models.mashi.mappers.toMashiDetails
import io.mashit.mashit.data.remote.apis.MashItApi
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
}