package com.mashiverse.mashit.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.PriceCurrency
import com.mashiverse.mashit.data.models.mashi.mappers.toNftsDetails
import com.mashiverse.mashit.data.remote.apis.MashitApi
import com.mashiverse.mashit.utils.MASHIT_KEY

class ShopPagingSource(
    private val api: MashitApi,
    private val apiKey: String = MASHIT_KEY
) : PagingSource<Int, Nft>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Nft> {
        val offset = params.key ?: 0
        val limit = params.loadSize

        return try {
            val response = api.getShopList(apiKey = apiKey, limit = limit, offset = offset)
            val listings = response.toNftsDetails()
            val hasMore = response.pagination.hasMore

            LoadResult.Page(
                data = listings,
                prevKey = if (offset == 0) null else maxOf(0, offset - limit),
                nextKey = when {
                    // TODO: POL showing and minting
                    listings.any { it.productInfo?.priceCurrency == PriceCurrency.POL } -> null
                    hasMore && listings.isNotEmpty() -> offset + listings.size
                    else -> null
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Nft>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(state.config.pageSize)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(state.config.pageSize)
        }
    }
}