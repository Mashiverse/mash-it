package com.mashiverse.mashit.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.mappers.toNfts
import com.mashiverse.mashit.data.remote.apis.MashitApi
import com.mashiverse.mashit.utils.MASHIT_KEY
import timber.log.Timber

class SearchPagingSource(
    val query: String,
    private val api: MashitApi,
    private val apiKey: String = MASHIT_KEY
) : PagingSource<Int, Nft>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Nft> {
        val offset = params.key ?: 0
        val limit = params.loadSize

        return try {
            val response = api.getSearchList(
                apiKey = apiKey,
                limit = limit,
                offset = offset,
                q = query
            )
            val listings = response.toNfts()
            val hasMore = response.pagination.hasMore

            LoadResult.Page(
                data = listings,
                prevKey = if (offset == 0) null else maxOf(0, offset - limit),
                nextKey = when {
                    hasMore && listings.isNotEmpty() -> offset + listings.size
                    else -> null
                }
            )
        } catch (e: Exception) {
            Timber.tag("Test").d(e)
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