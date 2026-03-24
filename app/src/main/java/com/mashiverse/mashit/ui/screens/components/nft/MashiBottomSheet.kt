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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.mashiverse.mashit.data.states.intents.ImageIntent
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.ui.screens.components.nft.trait.TraitHolder
import com.mashiverse.mashit.ui.screens.components.nft.trait.TraitImage
import com.mashiverse.mashit.ui.theme.BottomSheetShape
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.LargeMashiHolderHeight
import com.mashiverse.mashit.ui.theme.LargeMashiHolderWidth
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.ui.theme.Surface

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MashiBottomSheet(
    selectedNft: Nft,
    closeBottomShit: () -> Unit,
    processImageIntent: (ImageIntent) -> Unit,
    sheetState: SheetState,
    detailsContent: @Composable () -> Unit
) {
    val config = LocalConfiguration.current
    val mashiHolderWidth = (config.screenWidthDp.dp - 2 * Padding - 2 * SmallPadding) / 3
    val mashiHolderHeight = mashiHolderWidth * 4 / 3

    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth(),
        shape = BottomSheetShape,
        onDismissRequest = closeBottomShit,
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TraitImage(
                    modifier = Modifier
                        .height(LargeMashiHolderHeight)
                        .width(LargeMashiHolderWidth),
                    data = selectedNft.compositeUrl,
                    processImageIntent = processImageIntent
                )

                detailsContent()
            }

            Spacer(modifier = Modifier.height(Padding))

            Text(
                fontSize = 14.sp,
                text = "Traits",
                color = ContentAccentColor,
                fontWeight = FontWeight.Bold
            )

            selectedNft.traits?.let {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(SmallPadding),
                    horizontalArrangement = Arrangement.spacedBy(SmallPadding),
                    columns = GridCells.Fixed(3)
                ) {
                    items(selectedNft.traits.size) { i ->
                        TraitHolder(
                            trait = selectedNft.traits[i],
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