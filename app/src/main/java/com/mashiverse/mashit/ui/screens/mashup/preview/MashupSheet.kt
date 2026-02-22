package com.mashiverse.mashit.ui.screens.mashup.preview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.mashiverse.mashit.R
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.ui.screens.components.nft.trait.TraitHolder
import com.mashiverse.mashit.ui.theme.BottomSheetShape
import com.mashiverse.mashit.ui.theme.ContainerColor
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.PaddingSize
import com.mashiverse.mashit.ui.theme.SmallIconSize
import com.mashiverse.mashit.ui.theme.SmallPaddingSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MashupSheet(
    mashupDetails: MashupDetails,
    closeBottomShit: () -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope,
    getImageType: (String) -> ImageType?,
    setImageType: (ImageType, String) -> Unit,
    height: Dp
) {
    val config = LocalConfiguration.current
    val mashiHolderWidth =
        (config.screenWidthDp.dp - 2 * PaddingSize - 2 * SmallPaddingSize) / 3 - 0.2.dp
    val mashiHolderHeight = mashiHolderWidth * 4 / 3

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
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
                .height(height)
                .padding(start = PaddingSize, end = PaddingSize, top = PaddingSize)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    modifier = Modifier.size(SmallIconSize),
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                closeBottomShit.invoke()
                            }
                        }
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(SmallIconSize),
                        painter = painterResource(R.drawable.close_icon),
                        contentDescription = "More",
                        tint = ContentAccentColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(PaddingSize))

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    mashupDetails.assets.sortedBy { it.type }.let {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            verticalArrangement = Arrangement.spacedBy(SmallPaddingSize),
                            horizontalArrangement = Arrangement.spacedBy(SmallPaddingSize),
                            maxItemsInEachRow = 3
                        ) {
                            it.forEach { i ->
                                TraitHolder(
                                    trait = i,
                                    width = mashiHolderWidth,
                                    height = mashiHolderHeight,
                                    getImageType = getImageType,
                                    setImageType = setImageType
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}