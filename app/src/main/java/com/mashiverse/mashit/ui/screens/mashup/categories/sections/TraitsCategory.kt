package com.mashiverse.mashit.ui.screens.mashup.categories.sections

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.intents.ImageIntent
import com.mashiverse.mashit.data.intents.MashupIntent
import com.mashiverse.mashit.ui.screens.mashup.traits.MashupTraitHolder
import com.mashiverse.mashit.utils.helpers.sys.detectScreenType
import com.mashiverse.mashit.utils.helpers.sys.getItemWidthAndHeight

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun TraitsCategoryItems(
    lazyGridState: LazyGridState,
    traits: List<MashupTrait>,
    selectedTraitUrl: String,
    processMashupIntent: (MashupIntent) -> Unit,
    processImageIntent: (ImageIntent) -> Unit
) {
    val config = LocalConfiguration.current
    val screenType = config.detectScreenType()
    val (width, height) = config.getItemWidthAndHeight(screenType.collectionColumns, 12.dp)

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        state = lazyGridState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        columns = GridCells.Fixed(screenType.collectionColumns)
    ) {
        items(traits.size) { i ->
            MashupTraitHolder(
                height = height,
                width = width,
                isSelected = traits[i].trait.url == selectedTraitUrl,
                mashupTrait = traits[i],
                processMashupIntent = processMashupIntent,
                processImageIntent = processImageIntent
            )
        }
    }
}