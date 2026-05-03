package com.mashiverse.mashit.data.states.mashup

import com.mashiverse.mashit.data.models.sys.dialog.DialogContent
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.mashup.colors.ColorType
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.SortType
import com.mashiverse.mashit.data.models.mashi.TraitType

data class MashupUiState(
    val wallet: String? = null,
    val nfts: List<Nft> = emptyList(),
    val mashupDetails: MashupDetails = MashupDetails(),
    val selectedColorType: ColorType = ColorType.BASE,
    val selectedCategory: TraitType = TraitType.BACKGROUND,
    val sortType: SortType = SortType.NEWEST,
    val colors: SelectedColors = SelectedColors(),
    val dialogContent: DialogContent? = null,
    val isSave: Boolean = false,
    val isColorChange: Boolean = false,
    val isPreview: Boolean = false,
    val isCollectibles: Boolean = true,
    val isDownloading: Boolean = false,
    val isCollectionReady: Boolean = false
)