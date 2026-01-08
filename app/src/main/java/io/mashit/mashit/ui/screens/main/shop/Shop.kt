package io.mashit.mashit.ui.screens.main.shop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import io.mashit.mashit.ui.screens.main.header.CategoryHeader
import io.mashit.mashit.ui.screens.main.mashi.MashiBottomSheet
import io.mashit.mashit.ui.theme.PaddingSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shop() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isBottomSheet by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<ShopViewModel>()
    val listings by remember {
        viewModel.listings
    }
    val selectedMashi by remember {
        viewModel.selectedMashi
    }
    val selectId = { id: String ->
        viewModel.selectId(id)
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
                    selectId = selectId,
                    sectionItems = listings
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