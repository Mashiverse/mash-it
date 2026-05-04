package com.mashiverse.mashit.data.states.mashup

import com.mashiverse.mashit.data.models.sys.dialog.DialogContent

data class MashupUiState(
    val dialogContent: DialogContent? = null,
    val isSave: Boolean = false,
    val isColorChange: Boolean = false,
    val isPreview: Boolean = false,
    val isCollectibles: Boolean = true,
    val isDownloading: Boolean = false,
    val isCollectionReady: Boolean = false
)