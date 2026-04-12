package com.mashiverse.mashit.data.states

import androidx.paging.PagingData
import com.mashiverse.mashit.data.models.artists.ArtistPageInfo
import com.mashiverse.mashit.data.models.sys.dialog.DialogContent
import com.mashiverse.mashit.data.models.mashi.Nft
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class ArtistPageUiState(
    val wallet: String? = null,
    val itemsData: Flow<PagingData<Nft>> = flowOf(PagingData.empty()),
    val pageInfo: ArtistPageInfo? = null,
    val selectedNft: Nft? = null,
    val dialogContent: DialogContent? = null,
    val isExpanded: Boolean = false
)