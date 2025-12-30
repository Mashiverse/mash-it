package dev.tymoshenko.mashit.ui.screens.main.collection

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.tymoshenko.mashit.data.models.mashi.MashiDetails
import dev.tymoshenko.mashit.ui.screens.main.header.CategoryHeader
import dev.tymoshenko.mashit.ui.screens.main.mashi.MashiBottomSheet
import dev.tymoshenko.mashit.ui.screens.main.mashi.trait.Trait
import dev.tymoshenko.mashit.ui.theme.PaddingSize
import dev.tymoshenko.mashit.ui.theme.SmallPaddingSize


@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Collection() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isBottomSheet by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<CollectionViewModel>()
    val mashies by remember {
        derivedStateOf {
            viewModel.mashies.value
        }
    }

    val selectedMashi by remember {
        derivedStateOf {
            viewModel.selectedMashi.value
        }
    }
    val selectMashi = { mashi: MashiDetails ->
        viewModel.selectMashi(mashi)
        isBottomSheet = true
    }

    val closeBottomShit = {
        isBottomSheet = false
    }

    val config = LocalConfiguration.current
    val mashiHolderWidth = (config.screenWidthDp.dp - 2 * PaddingSize - 2 * SmallPaddingSize) / 3
    val mashiHolderHeight = mashiHolderWidth * 4 / 3


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PaddingSize),
    ) {
        CategoryHeader("Collection")

        Spacer(modifier = Modifier.height(PaddingSize))

        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(SmallPaddingSize),
            horizontalArrangement = Arrangement.spacedBy(SmallPaddingSize),
            columns = GridCells.Fixed(3)
        ) {
            items(mashies.size) { i ->
                Trait(
                    onClick = { selectMashi.invoke(mashies[i]) },
                    width = mashiHolderWidth,
                    height = mashiHolderHeight,
                    data = mashies[i].compositeUrl,
                )
            }
        }
    }

    if (isBottomSheet) {
        selectedMashi?.let {
            MashiBottomSheet(
                selectedMashi = selectedMashi!!,
                sheetState = sheetState,
                closeBottomShit = closeBottomShit,
                scope = scope
            )
        }
    }
}