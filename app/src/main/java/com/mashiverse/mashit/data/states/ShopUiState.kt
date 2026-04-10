package com.mashiverse.mashit.data.states

import androidx.paging.PagingData
import com.mashiverse.mashit.data.models.dialog.DialogContent
import com.mashiverse.mashit.data.models.nft.Nft
import kotlinx.coroutines.flow.Flow

data class ShopUiState(
    val wallet: String,
    val itemsData: Flow<PagingData<Nft>>?,
    val categoryName: String,
    val selectedId: String,
    val dialogContent: DialogContent? = null,
    val isExpanded: Boolean
)