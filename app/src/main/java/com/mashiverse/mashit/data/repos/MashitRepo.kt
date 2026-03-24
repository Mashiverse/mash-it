package com.mashiverse.mashit.data.repos

import SaveMashupRes
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.data.models.mashup.save.MashupColors
import com.mashiverse.mashit.data.models.mashup.save.MashupData
import com.mashiverse.mashit.data.models.mashup.save.MashupLayer
import com.mashiverse.mashit.data.models.mashup.save.SaveMashupReq
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.models.nft.Trait
import com.mashiverse.mashit.data.models.nft.TraitType
import com.mashiverse.mashit.data.models.nft.mappers.toNft
import com.mashiverse.mashit.data.models.nft.mappers.toNfts
import com.mashiverse.mashit.data.remote.apis.MashitApi
import com.mashiverse.mashit.data.remote.paging.ShopPagingSource
import com.mashiverse.mashit.utils.helpers.toFilebaseUri
import com.mashiverse.mashit.utils.helpers.toIpfsUri
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class MashitRepo @Inject constructor(
    private val mashitApi: MashitApi,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getShopListPagingData(pageSize: Int = 20): Flow<PagingData<Nft>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = pageSize
            ),
            pagingSourceFactory = { ShopPagingSource(api = mashitApi) }
        ).flow
    }

    // TODO: Replace with search api
    suspend fun getAllListings(): List<Nft> {
        val allListings = mutableListOf<Nft>()
        var currentOffset = 0
        var hasMore = true
        val limit = 60

        try {
            while (hasMore) {
                val response = mashitApi.getShopList(
                    limit = limit,
                    offset = currentOffset
                )

                val listings = response.toNfts()
                allListings.addAll(listings)

                hasMore = response.pagination.hasMore &&
                        listings.isNotEmpty()

                if (hasMore) {
                    currentOffset += listings.size
                }
            }
        } catch (e: Exception) {
            Timber.tag("Test").d(e)
        }

        return allListings
    }

    suspend fun getShopItem(
        id: String
    ): Nft {
        try {
            val listingDto = mashitApi.getShopItem(id)
            return listingDto.toNft()
        } catch (_: Exception) {
            return Nft(
                name = "Unknown",
                author = "Unknown",
                compositeUrl = "",
                description = null
            )
        }
    }

    suspend fun saveMashup(
        mashupDetails: MashupDetails,
        wallet: String
    ): SaveMashupRes? {
        val assets = mashupDetails.assets
        val layers = assets
            .filter { it.url != null }
            .map { asset ->
                MashupLayer(
                    name = asset.type.name.lowercase(),
                    image = asset.url!!.toIpfsUri()
                )
            }

        val colors = mashupDetails.colors
        val mashupColors = MashupColors(
            base = colors.base,
            eyes = colors.eyes,
            hair = colors.hair
        )
        val mashupData = MashupData(
            colors = mashupColors,
            layers = layers
        )

        val req = SaveMashupReq(
            walletAddress = wallet,
            mashup = mashupData
        )
        return try {
            mashitApi.saveMashup(request = req)
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getMashup(
        wallet: String
    ): MashupDetails {
        try {
            val mashupDto = mashitApi.getMashup(wallet)

            val traits = List(11) { i ->
                Trait(
                    url = null,
                    type = TraitType.entries[i]
                )
            }.toMutableList()

            mashupDto.assets.forEach { asset ->
                val assetToUpdate =
                    traits.firstOrNull { TraitType.valueOf(asset.name.uppercase()) == it.type }
                val i = traits.indexOf(assetToUpdate)
                traits[i] = Trait(
                    type = TraitType.valueOf(asset.name.uppercase()),
                    url = asset.image.toFilebaseUri()
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
        } catch (_: Exception) {
            return MashupDetails()
        }
    }
}