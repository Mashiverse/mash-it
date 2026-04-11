package com.mashiverse.mashit.ui.nft

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.intents.ImageIntent
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.models.nft.OptionalTrait
import com.mashiverse.mashit.data.models.nft.Trait
import com.mashiverse.mashit.data.models.nft.TraitType
import com.mashiverse.mashit.ui.theme.BottomSheetShape
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.LargeHolderHeight
import com.mashiverse.mashit.ui.theme.LargeHolderWidth
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.Surface
import com.mashiverse.mashit.utils.helpers.sys.detectScreenType
import com.mashiverse.mashit.utils.helpers.sys.getItemWidthAndHeight

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MashiBottomSheet(
    selectedNft: Nft,
    closeBottomSheet: () -> Unit,
    processImageIntent: (ImageIntent) -> Unit,
    sheetState: SheetState,
    detailsContent: @Composable () -> Unit
) {
    val config = LocalConfiguration.current
    val screenType = config.detectScreenType()
    val (width, _) = config.getItemWidthAndHeight(screenType.collectionColumns, 12.dp)

    val optionalTraits = remember(selectedNft) {
        selectedNft.traits?.map { trait ->
            OptionalTrait(trait = trait, selected = true)
        }?.toMutableStateList()
    }

    val selectTrait = { trait: Trait ->
        val index = optionalTraits?.indexOfFirst { it.trait == trait } ?: -1
        if (index != -1) {
            val item = optionalTraits!![index]
            if (item.trait.type != TraitType.BACKGROUND) {
                optionalTraits[index] = item.copy(selected = !item.selected)
            }
        }
    }

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        shape = BottomSheetShape,
        onDismissRequest = closeBottomSheet,
        sheetState = sheetState,
        containerColor = Surface,
        contentColor = ContentColor,
        dragHandle = null,
        sheetGesturesEnabled = false
    ) {
        Column(
            modifier = Modifier
                .height((config.screenHeightDp * 0.8).dp)
                .fillMaxWidth()
                .padding(start = Padding, end = Padding, top = Padding),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(Padding)) {
                val selectedTraits =
                    optionalTraits?.filter { it.selected }?.map { it.trait } ?: emptyList()

                MashupComposite(
                    modifier = Modifier
                        .height(LargeHolderHeight)
                        .width(LargeHolderWidth),
                    assets = selectedTraits,
                    holderWidth = LargeHolderWidth,
                    processImageIntent = processImageIntent
                )

                detailsContent()
            }

            Spacer(modifier = Modifier.height(Padding))

            optionalTraits?.let { traits ->
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(Padding),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    columns = GridCells.Fixed(screenType.collectionColumns)
                ) {
                    items(traits.size) { i ->
                        val isSelected = traits[i].selected
                        TraitHolder(
                            modifier = Modifier.width(width),
                            onClick = { selectTrait(traits[i].trait) },
                            isSelected = isSelected,
                            trait = traits[i].trait,
                            processImageIntent = processImageIntent
                        )
                    }
                }
            }
        }
    }
}