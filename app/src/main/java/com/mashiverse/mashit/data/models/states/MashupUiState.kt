package com.mashiverse.mashit.data.models.states

import com.mashiverse.mashit.data.models.dialog.DialogContent
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.mashup.colors.ColorType

data class MashupUiState(
    val wallet: String? = null,
    val mashupDetails: MashupDetails = MashupDetails(),
    val selectedColorType: ColorType = ColorType.BASE,
    val dialogContent: DialogContent? = null,
    val isColorChange: Boolean = false,
    val isPreview: Boolean = false
)
