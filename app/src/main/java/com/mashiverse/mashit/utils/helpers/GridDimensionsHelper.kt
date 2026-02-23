package com.mashiverse.mashit.utils.helpers

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.floor

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun GridDimensionsHelper(
    minWidth: Dp,
    maxWidth: Dp,
    horizontalPadding: Dp,
    gridGap: Dp,
    aspectRatio: Float = 3f / 4f,
    onDimensionsCalculated: (itemWidth: Dp, itemHeight: Dp, columns: Int) -> Unit
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp

    LaunchedEffect(screenWidth, minWidth, maxWidth, horizontalPadding, gridGap) {
        val availableWidth = screenWidth - (horizontalPadding * 2)

        val columns = floor((availableWidth + gridGap) / (minWidth + gridGap))
            .toInt()
            .coerceAtLeast(1)

        val totalGaps = gridGap * (columns - 1)
        val calculatedWidth = (availableWidth - totalGaps) / columns

        val finalWidth = calculatedWidth.coerceIn(minWidth, maxWidth)
        val finalHeight = finalWidth / aspectRatio

        onDimensionsCalculated(finalWidth, finalHeight, columns)
    }
}