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
import com.mashiverse.mashit.data.models.ScreenType
import com.mashiverse.mashit.data.states.intents.ImageIntent
import com.mashiverse.mashit.data.states.intents.MashupIntent
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.ui.screens.mashup.MashupTraitHolder
import com.mashiverse.mashit.ui.theme.MaxShopCategoryItemWidth
import com.mashiverse.mashit.ui.theme.MinShopCategoryItemWidth
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.utils.helpers.detectScreenType
import com.mashiverse.mashit.utils.helpers.getItemWidthAndHeight
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
    val screenType = config.detectScreenType()
    val columnCount = if (screenType == ScreenType.COMPACT) 3 else 5
    val (width, height) = config.getItemWidthAndHeight(screenType, 3, 5)

    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        state = lazyGridState,
        verticalArrangement = Arrangement.spacedBy(Padding),
        horizontalArrangement = Arrangement.spacedBy(SmallPadding),
        columns = GridCells.Fixed(columnCount)
    ) {
        items(traits.size) { i ->
            MashupTraitHolder(
                height = height,
                width = width,
                mashupTrait = traits[i],
                processMashupIntent = processMashupIntent,
                processImageIntent = processImageIntent
            )
        }
    }
}