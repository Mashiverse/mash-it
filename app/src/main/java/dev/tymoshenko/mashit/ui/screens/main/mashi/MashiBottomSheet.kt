package dev.tymoshenko.mashit.ui.screens.main.mashi

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.tymoshenko.mashit.ui.screens.main.shop.ShopViewModel
import dev.tymoshenko.mashit.ui.theme.BottomSheetShape
import dev.tymoshenko.mashit.ui.theme.ContainerColor
import dev.tymoshenko.mashit.ui.theme.ContentAccentColor
import dev.tymoshenko.mashit.ui.theme.ContentColor
import dev.tymoshenko.mashit.ui.theme.PaddingSize
import dev.tymoshenko.mashit.ui.theme.SmallPaddingSize
import kotlinx.coroutines.CoroutineScope

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MashiBottomSheet(
    closeBottomShit: () -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope
) {
    val config = LocalConfiguration.current
    val mashiHolderWidth = (config.screenWidthDp.dp - 2 * PaddingSize - 2 * SmallPaddingSize) / 3
    val mashiHolderHeight = mashiHolderWidth * 4 / 3

    val viewModel = hiltViewModel<ShopViewModel>()
    val mashiDetails by remember {
        derivedStateOf {
            viewModel.selectedMashi.value
        }
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
                .height((config.screenHeightDp * 0.8).dp)
                .fillMaxWidth()
                .padding(start = PaddingSize, end = PaddingSize, top = PaddingSize),
        ) {
            MashiDetailsSection(
                mashiDetails = mashiDetails!!,
                scope = scope,
                sheetState = sheetState,
                closeBottomShit = closeBottomShit
            )

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
                items(mashiDetails!!.traits.size) { i ->
                    MashiTraitHolder(
                        trait = mashiDetails!!.traits[i],
                        width = mashiHolderWidth,
                        height = mashiHolderHeight
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun MashiBottomSheetPreview() {
    MashiBottomSheet(
        closeBottomShit = {},
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        scope = rememberCoroutineScope()
    )
}