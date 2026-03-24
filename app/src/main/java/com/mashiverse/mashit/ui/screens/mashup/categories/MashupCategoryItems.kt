package com.mashiverse.mashit.ui.screens.mashup.categories

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.mashiverse.mashit.data.states.intents.ImageIntent
import com.mashiverse.mashit.data.states.intents.MashupIntent
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.ui.screens.mashup.MashupTraitHolder
import com.mashiverse.mashit.ui.theme.MaxShopCategoryItemWidth
import com.mashiverse.mashit.ui.theme.MinShopCategoryItemWidth
import com.mashiverse.mashit.ui.theme.PaddingSize
import com.mashiverse.mashit.ui.theme.SmallPaddingSize
import kotlin.math.floor

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MashupCategoryItems(
    modifier: Modifier,
    lazyGridState: LazyGridState,
    traits: List<MashupTrait>,
    processMashupIntent: (MashupIntent) -> Unit,
    processImageIntent: (ImageIntent) -> Unit
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp

    // 1. Calculate how many columns we CAN fit using the Minimum width
    // Formula: (Available Width + Gap) / (Min Width + Gap)
    val availableWidth = screenWidth - (2 * PaddingSize)
    val columns = floor((availableWidth + SmallPaddingSize) / (MinShopCategoryItemWidth + SmallPaddingSize))
        .toInt()
        .coerceAtLeast(1)

    // 2. Calculate the dynamic width for each item to fill the row perfectly
    // This ensures no awkward empty space on the right
    val totalGaps = (columns - 1) * SmallPaddingSize
    val calculatedWidth = (availableWidth - totalGaps) / columns

    // 3. Clamp the width and height between your defined Min and Max
    val finalWidth = calculatedWidth.coerceIn(MinShopCategoryItemWidth, MaxShopCategoryItemWidth)

    // Maintain the 4:3 aspect ratio or use your Min/Max height constants
    val finalHeight = (finalWidth / 3) * 4


    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        state = lazyGridState,
        verticalArrangement = Arrangement.spacedBy(PaddingSize),
        horizontalArrangement = Arrangement.spacedBy(SmallPaddingSize),
        columns = GridCells.Fixed(columns)
    ) {
        items(traits.size) { i ->
            MashupTraitHolder(
                height = finalHeight,
                width = finalWidth,
                mashupTrait = traits[i],
                processMashupIntent = processMashupIntent,
                processImageIntent = processImageIntent
            )
        }
    }
}