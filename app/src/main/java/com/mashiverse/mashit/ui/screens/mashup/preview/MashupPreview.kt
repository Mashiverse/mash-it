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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.intents.ImageIntent
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.ui.nft.TraitHolder
import com.mashiverse.mashit.ui.theme.BottomSheetShape
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.ui.theme.Surface
import com.mashiverse.mashit.utils.helpers.sys.detectScreenType
import com.mashiverse.mashit.utils.helpers.sys.getItemWidthAndHeight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MashupPreview(
    mashupDetails: MashupDetails,
    closeBottomSheet: () -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope,
    processImageIntent: (ImageIntent) -> Unit,
    height: Dp
) {
    val config = LocalConfiguration.current
    val screenType = config.detectScreenType()
    val (itemWidth, _) = config.getItemWidthAndHeight(screenType.collectionColumns, 12.dp)

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
                .height(height)
                .padding(start = Padding, end = Padding, top = Padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Spacer(Modifier.weight(1F))

                IconButton(
                    modifier = Modifier.size(32.dp),
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                closeBottomSheet.invoke()
                            }
                        }
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(32.dp),
                        imageVector = Icons.Default.Clear,
                        contentDescription = "More",
                        tint = ContentAccentColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(SmallPadding))

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    mashupDetails.assets.sortedBy { it.type }.let {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(11.5.dp),
                            maxItemsInEachRow = screenType.collectionColumns
                        ) {
                            it.forEach { i ->
                                TraitHolder(
                                    modifier = Modifier.width(itemWidth),
                                    trait = i,
                                    processImageIntent = processImageIntent,
                                    onClick = {}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}