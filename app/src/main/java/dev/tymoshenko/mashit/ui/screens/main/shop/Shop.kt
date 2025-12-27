package dev.tymoshenko.mashit.ui.screens.main.shop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.tymoshenko.mashit.data.models.mashi.MashiDetails
import dev.tymoshenko.mashit.ui.screens.main.header.CategoryHeader
import dev.tymoshenko.mashit.ui.screens.main.mashi.MashiBottomSheet
import dev.tymoshenko.mashit.ui.screens.main.shop.mashi.ShopDetailsSection
import dev.tymoshenko.mashit.ui.theme.PaddingSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shop() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isBottomSheet by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<ShopViewModel>()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PaddingSize)
    ) {
        CategoryHeader(title = "Shop")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(PaddingSize)
        ) {
            item {
                ShopSection(
                    sectionName = "Ervindas",
                    onMashiClick = selectMashi,
                    sectionItems = mashies
                )
            }
        }
    }

    if (isBottomSheet) {
        selectedMashi?.let {
            MashiBottomSheet(
                selectedMashi = selectedMashi!!,
                sheetState = sheetState,
                closeBottomShit = closeBottomShit
            ) {
                ShopDetailsSection(
                    mashiDetails = selectedMashi!!,
                    scope = scope,
                    sheetState = sheetState,
                    closeBottomShit = closeBottomShit
                )
            }
        }
    }
}