package com.mashiverse.mashit.data.repos

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mashiverse.mashit.data.models.color.SelectedColors
import com.mashiverse.mashit.data.models.mashi.MashupDetails
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.Trait
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.data.models.mashi.mappers.toNftDetails
import com.mashiverse.mashit.data.remote.apis.MashitApi
import com.mashiverse.mashit.data.remote.paging.ShopPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MashitRepo @Inject constructor(
    private val mashitApi: MashitApi,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getShopListPagingData(pageSize: Int = 20): Flow<PagingData<Nft>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                prefetchDistance = 5, // Trigger next load 5 items before end
                enablePlaceholders = false,
                initialLoadSize = pageSize
            ),
            pagingSourceFactory = { ShopPagingSource(api = mashitApi) }
        ).flow
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