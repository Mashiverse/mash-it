package com.mashiverse.mashit.ui.screens.components.nft

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.models.nft.OptionalTrait
import com.mashiverse.mashit.data.models.nft.Trait
import com.mashiverse.mashit.data.models.nft.TraitType
import com.mashiverse.mashit.data.states.intents.ImageIntent
import com.mashiverse.mashit.ui.screens.components.nft.trait.TraitHolder
import com.mashiverse.mashit.ui.theme.BottomSheetShape
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ShopHolderHeight
import com.mashiverse.mashit.ui.theme.ShopHolderWidth
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MashiBottomSheet(
    selectedNft: Nft,
    closeBottomSheet: () -> Unit, // Renamed from closeBottomSheet
    processImageIntent: (ImageIntent) -> Unit,
    sheetState: SheetState,
    detailsContent: @Composable () -> Unit
) {
    val config = LocalConfiguration.current
    val mashiHolderWidth = (config.screenWidthDp.dp - 2 * Padding - 2 * 12.dp) / 3
    val mashiHolderHeight = mashiHolderWidth * 4 / 3

    // FIX 1 & 3: Use toMutableStateList and add selectedNft as a key
    // so the state resets if the NFT changes.
    val optionalTraits = remember(selectedNft) {
        selectedNft.traits?.map { trait ->
            OptionalTrait(trait = trait, selected = true)
        }?.toMutableStateList() // This makes the list observable
    }

    // FIX 2: Updated selection logic to work with Compose state
    val selectTrait = { trait: Trait ->
        val index = optionalTraits?.indexOfFirst { it.trait == trait } ?: -1
        if (index != -1) {
            val item = optionalTraits!![index]
            // We replace the element to trigger the observable list update
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
        containerColor = Color(32, 32, 32),
        contentColor = ContentColor,
        dragHandle = null,
        // Warning: Ensure you really want gestures disabled,
        // otherwise users can't swipe down to close.
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
                        .height(ShopHolderHeight)
                        .width(ShopHolderWidth),
                    assets = selectedTraits,
                    holderWidth = ShopHolderWidth,
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
                    columns = GridCells.Fixed(3)
                ) {
                    items(traits.size) { i ->
                        val isSelected = traits[i].selected
                        TraitHolder(
                            onClick = {
                                selectTrait(traits[i].trait)
                            },
                            isSelected = isSelected,
                            // Pass selection state to TraitHolder if it supports visual feedback
                            trait = traits[i].trait,
                            width = mashiHolderWidth,
                            height = mashiHolderHeight,
                            processImageIntent = processImageIntent
                        )
                    }
                }
            }
        }
    }
}