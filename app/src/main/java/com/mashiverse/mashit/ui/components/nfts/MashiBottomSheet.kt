package com.mashiverse.mashit.ui.components.nfts

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.ui.components.nfts.trait.TraitHolder
import com.mashiverse.mashit.ui.components.nfts.trait.TraitImage
import com.mashiverse.mashit.ui.theme.BottomSheetShape
import com.mashiverse.mashit.ui.theme.ContainerColor
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.LargeMashiHolderHeight
import com.mashiverse.mashit.ui.theme.LargeMashiHolderWidth
import com.mashiverse.mashit.ui.theme.MaxCollectionItemWidth
import com.mashiverse.mashit.ui.theme.MinCollectionItemWidth
import com.mashiverse.mashit.ui.theme.PaddingSize
import com.mashiverse.mashit.ui.theme.SmallPaddingSize
import com.mashiverse.mashit.utils.helpers.GridDimensionsHelper

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MashiBottomSheet(
    selectedNft: Nft,
    closeBottomShit: () -> Unit,
    getImageType: (String) -> ImageType?,
    setImageType: (ImageType, String) -> Unit,
    sheetState: SheetState,
    detailsContent: @Composable () -> Unit
) {
    val config = LocalConfiguration.current
    val maxSheetHeight = (config.screenHeightDp * 0.8).dp

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

    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth(),
        shape = BottomSheetShape,
        onDismissRequest = closeBottomShit,
        sheetState = sheetState,
        containerColor = ContainerColor,
        contentColor = ContentColor,
        dragHandle = null,
        sheetGesturesEnabled = false
    ) {
        Column(
            modifier = Modifier
                .heightIn(max = maxSheetHeight)
                .fillMaxWidth()
                .padding(start = PaddingSize, end = PaddingSize, top = PaddingSize),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TraitImage(
                    modifier = Modifier
                        .height(LargeMashiHolderHeight)
                        .width(LargeMashiHolderWidth),
                    data = selectedNft.compositeUrl,
                    getImageType = getImageType,
                    setImageType = setImageType
                )

                detailsContent()
            }

            Spacer(modifier = Modifier.height(PaddingSize))

            Text(
                fontSize = 14.sp,
                text = "Traits",
                color = ContentAccentColor,
                fontWeight = FontWeight.Bold
            )

            selectedNft.traits?.let {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(SmallPaddingSize),
                    horizontalArrangement = Arrangement.spacedBy(SmallPaddingSize),
                    columns = GridCells.Fixed(columns)
                ) {
                    items(selectedNft.traits.size) { i ->
                        TraitHolder(
                            trait = selectedNft.traits[i],
                            width = width,
                            height = height,
                            getImageType = getImageType,
                            setImageType = setImageType
                        )
                    }
                }
            }
        }
    }
}