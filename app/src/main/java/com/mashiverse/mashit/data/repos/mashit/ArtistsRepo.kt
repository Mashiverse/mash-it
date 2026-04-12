package com.mashiverse.mashit.data.repos.mashit

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mashiverse.mashit.data.models.artists.ArtistInfo
import com.mashiverse.mashit.data.models.artists.mappers.toArtistPage
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.remote.apis.MashitApi
import com.mashiverse.mashit.data.remote.paging.ArtistListingsPagingSource
import com.mashiverse.mashit.data.remote.paging.ArtistsPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtistsRepo @Inject constructor(
    private val mashitApi: MashitApi,
) {

    fun getArtistsPagingData(pageSize: Int = 20): Flow<PagingData<ArtistInfo>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = pageSize
            ),
            pagingSourceFactory = { ArtistsPagingSource(api = mashitApi) }
        ).flow
    }

    fun getListingsPagingData(
        alias: String,
        pageSize: Int = 20
    ) : Flow<PagingData<Nft>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = pageSize
            ),
            pagingSourceFactory = {
                ArtistListingsPagingSource(
                    api = mashitApi,
                    alias = alias
                )
            }
        ).flow
    }

    suspend fun getArtistsPage(alias: String) = mashitApi.getArtistPage(alias).toArtistPage()
}