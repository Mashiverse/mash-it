package com.mashiverse.mashit.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mashiverse.mashit.data.models.artists.ArtistInfo
import com.mashiverse.mashit.data.models.artists.mappers.toArtists
import com.mashiverse.mashit.data.remote.apis.MashitApi
import com.mashiverse.mashit.utils.MASHIT_KEY
import timber.log.Timber

class ArtistsPagingSource(
    private val api: MashitApi,
    private val apiKey: String = MASHIT_KEY
) : PagingSource<Int, ArtistInfo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtistInfo> {
        val offset = params.key ?: 0
        val limit = params.loadSize

        return try {
            val response = api.getArtists(
                apiKey = apiKey,
                limit = limit,
                offset = offset,
            )
            val artists = response.toArtists()
            val hasMore = response.pagination.hasMore

            LoadResult.Page(
                data = artists,
                prevKey = if (offset == 0) null else maxOf(0, offset - limit),
                nextKey = when {
                    hasMore && artists.isNotEmpty() -> offset + artists.size
                    else -> null
                }
            )
        } catch (e: Exception) {
            Timber.tag("Test").d(e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArtistInfo>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(state.config.pageSize)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(state.config.pageSize)
        }
    }
}