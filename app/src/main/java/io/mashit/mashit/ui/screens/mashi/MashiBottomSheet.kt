package io.mashit.mashit.ui.screens.mashi

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
import io.mashit.mashit.data.models.mashi.MashiDetails
import io.mashit.mashit.ui.screens.mashi.trait.TraitHolder
import io.mashit.mashit.ui.screens.mashi.trait.Trait
import io.mashit.mashit.ui.theme.BottomSheetShape
import io.mashit.mashit.ui.theme.ContainerColor
import io.mashit.mashit.ui.theme.ContentAccentColor
import io.mashit.mashit.ui.theme.ContentColor
import io.mashit.mashit.ui.theme.LargeMashiHolderHeight
import io.mashit.mashit.ui.theme.LargeMashiHolderWidth
import io.mashit.mashit.ui.theme.PaddingSize
import io.mashit.mashit.ui.theme.SmallPaddingSize
import kotlinx.coroutines.CoroutineScope

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MashiBottomSheet(
    selectedMashi: MashiDetails,
    scope: CoroutineScope,
    closeBottomShit: () -> Unit,
    sheetState: SheetState
) {
    val config = LocalConfiguration.current
    val mashiHolderWidth = (config.screenWidthDp.dp - 2 * PaddingSize - 2 * SmallPaddingSize) / 3
    val mashiHolderHeight = mashiHolderWidth * 4 / 3

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
                .height((config.screenHeightDp * 0.8).dp)
                .fillMaxWidth()
                .padding(start = PaddingSize, end = PaddingSize, top = PaddingSize),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Trait(
                    modifier = Modifier
                        .height(LargeMashiHolderHeight)
                        .width(LargeMashiHolderWidth),
                    data = selectedMashi.compositeUrl,
                )

                MashiDetailsSection(
                    mashiDetails = selectedMashi,
                    scope = scope,
                    closeBottomShit = closeBottomShit,
                    sheetState = sheetState
                )
            }

            Spacer(modifier = Modifier.height(PaddingSize))

            Text(
                fontSize = 14.sp,
                text = "Traits",
                color = ContentAccentColor,
                fontWeight = FontWeight.Bold
            )

            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(SmallPaddingSize),
                horizontalArrangement = Arrangement.spacedBy(SmallPaddingSize),
                columns = GridCells.Fixed(3)
            ) {
                items(selectedMashi.mashiTraits.size) { i ->
                    TraitHolder(
                        mashiTrait = selectedMashi.mashiTraits[i],
                        width = mashiHolderWidth,
                        height = mashiHolderHeight
                    )
                }
            }
        }
    }
}