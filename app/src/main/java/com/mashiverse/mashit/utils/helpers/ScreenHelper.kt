package com.mashiverse.mashit.utils.helpers

import android.content.res.Configuration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.mashiverse.mashit.data.models.ScreenType
import com.mashiverse.mashit.ui.theme.Padding

fun Configuration.detectScreenType(): ScreenType {
    return when {
        this.screenWidthDp < 600 -> ScreenType.COMPACT
        this.screenWidthDp < 840 -> ScreenType.MEDIUM
        else -> ScreenType.EXPANDED
    }
}

fun Configuration.getItemWidthAndHeight(screenType: ScreenType, min: Int, max: Int): Pair<Dp, Dp> {
    val columnCount = if (screenType == ScreenType.COMPACT) min else max

    val mashiHolderWidth =
        (this.screenWidthDp.dp - 2 * Padding - (columnCount - 1) * 8.dp) / columnCount
    val mashiHolderHeight = mashiHolderWidth * 4 / 3

    return Pair(mashiHolderWidth, mashiHolderHeight)
}
