package io.mashit.mashit.ui.screens.mashup

import android.annotation.SuppressLint
import android.app.LocaleConfig
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
import io.mashit.mashit.data.models.mashi.MashiTrait
import io.mashit.mashit.data.models.mashi.MashupTrait
import io.mashit.mashit.ui.screens.mashi.trait.MashupTraitHolder
import io.mashit.mashit.ui.theme.PaddingSize
import io.mashit.mashit.ui.theme.SmallPaddingSize

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MashupCategoryItems(
    lazyGridState: LazyGridState,
    traits: List<MashupTrait>,
    changeMashupTrait: (MashiTrait) -> Unit
) {
    val config = LocalConfiguration.current
    val mashiHolderWidth =
        (config.screenWidthDp.dp - (2.0 * PaddingSize) - 2 * SmallPaddingSize) / 3 - 1.dp
    val mashiHolderHeight = mashiHolderWidth * 4 / 3

    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        state = lazyGridState,
        verticalArrangement = Arrangement.spacedBy(PaddingSize),
        horizontalArrangement = Arrangement.spacedBy(SmallPaddingSize),
        columns = GridCells.Fixed(3)
    ) {
        items(traits.size) { i ->
            MashupTraitHolder(
                mashiHolderHeight = mashiHolderHeight,
                mashiHolderWidth = mashiHolderWidth,
                trait = traits[i],
                changeMashupTrait = changeMashupTrait
            )
        }
    }
}