package io.mashit.mashit.ui.screens.mashup.preview

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import io.mashit.mashit.R
import io.mashit.mashit.data.models.mashi.MashupDetails
import io.mashit.mashit.ui.screens.mashi.trait.TraitHolder
import io.mashit.mashit.ui.screens.mashup.MashupComposite
import io.mashit.mashit.ui.theme.BottomSheetShape
import io.mashit.mashit.ui.theme.ContainerColor
import io.mashit.mashit.ui.theme.ContentAccentColor
import io.mashit.mashit.ui.theme.ContentColor
import io.mashit.mashit.ui.theme.PaddingSize
import io.mashit.mashit.ui.theme.SmallIconSize
import io.mashit.mashit.ui.theme.SmallPaddingSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MashupPreview(
    mashupDetails: MashupDetails,
    closeBottomShit: () -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope,
) {
    val config = LocalConfiguration.current
    val mashiHolderWidth = (config.screenWidthDp.dp - 2 * PaddingSize - 2 * SmallPaddingSize) / 3 - 1.dp
    val mashiHolderHeight = mashiHolderWidth * 4 / 3

    val compositeHolderWidth = (config.screenWidthDp.dp - 2 * PaddingSize)
    val compositeHolderHeight = compositeHolderWidth * 4 / 3

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
        LazyColumn(
            modifier = Modifier
                .height((config.screenHeightDp * 0.8).dp)
                .fillMaxWidth()
                .padding(start = PaddingSize, end = PaddingSize, top = PaddingSize),
        ) {
            item {
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

                MashupComposite(
                    mashupDetails = mashupDetails,
                    modifier = Modifier
                        .width(compositeHolderWidth)
                        .height(compositeHolderHeight),
                    holderWidth = compositeHolderWidth
                )

                Spacer(modifier = Modifier.height(PaddingSize))

                mashupDetails.assets?.sortedBy { it.traitType }.let {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth()
                            .wrapContentHeight(),
                        verticalArrangement = Arrangement.spacedBy(SmallPaddingSize),
                        horizontalArrangement = Arrangement.spacedBy(SmallPaddingSize),
                        maxItemsInEachRow = 3
                    ) {
                        it?.forEach { i ->
                            TraitHolder(
                                mashiTrait = i,
                                width = mashiHolderWidth,
                                height = mashiHolderHeight
                            )
                        }
                    }
                }
            }
        }
    }
}