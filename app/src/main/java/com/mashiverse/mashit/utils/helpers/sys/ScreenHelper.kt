package com.mashiverse.mashit.utils.helpers.sys

import android.content.res.Configuration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.mashiverse.mashit.data.models.sys.screens.ScreenInfo
import com.mashiverse.mashit.ui.theme.Padding

fun Configuration.detectScreenType(): ScreenInfo {
    return when {
        this.screenWidthDp < 600 -> ScreenInfo.COMPACT
        this.screenWidthDp >= 1200 -> ScreenInfo.EXPANDED
        else -> ScreenInfo.MEDIUM
    }
}

fun getItemWidthAndHeight(columns: Int, maxWidth: Dp, padding: Dp = Padding, initialPadding: Dp = 0.dp): Pair<Dp, Dp> {
    val width = (maxWidth - 2 * Padding - (columns - 1) * padding - 2 * initialPadding) / columns
    val height = width * 4 / 3
    return Pair(width, height)
}
