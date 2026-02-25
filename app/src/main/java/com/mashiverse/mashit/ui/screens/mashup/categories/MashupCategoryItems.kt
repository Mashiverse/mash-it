package com.mashiverse.mashit.ui.screens.mashup.categories

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.ui.screens.mashup.MashupTraitHolder
import com.mashiverse.mashit.ui.theme.MaxCollectionItemWidth
import com.mashiverse.mashit.ui.theme.MinCollectionItemWidth
import com.mashiverse.mashit.ui.theme.PaddingSize
import com.mashiverse.mashit.ui.theme.SmallPaddingSize
import com.mashiverse.mashit.utils.helpers.ui.GridDimensionsHelper

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MashupCategoryItems(
    modifier: Modifier,
    lazyGridState: LazyGridState,
    traits: List<MashupTrait>,
    changeMashupTrait: (MashupTrait) -> Unit,
    getImageType: (String) -> ImageType?,
    setImageType: (ImageType, String) -> Unit,
) {
    var columns by remember { mutableIntStateOf(1) }
    var width by remember { mutableStateOf(0.dp) }
    var height by remember { mutableStateOf(0.dp) }

    GridDimensionsHelper(
        minWidth = MinCollectionItemWidth,
        maxWidth = MaxCollectionItemWidth,
        horizontalPadding = PaddingSize,
        gridGap = SmallPaddingSize,
    ) { w: Dp, h: Dp, col: Int ->
        columns = col
        width = w
        height = h
    }

    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        state = lazyGridState,
        verticalArrangement = Arrangement.spacedBy(PaddingSize),
        horizontalArrangement = Arrangement.spacedBy(SmallPaddingSize),
        columns = GridCells.Fixed(columns)
    ) {
        items(traits.size) { i ->
            MashupTraitHolder(
                height = height,
                width = width,
                mashupTrait = traits[i],
                changeMashupTrait = changeMashupTrait,
                getImageType = getImageType,
                setImageType = setImageType
            )
        }
    }
}