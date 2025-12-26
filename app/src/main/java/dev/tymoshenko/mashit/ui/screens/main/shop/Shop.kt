package dev.tymoshenko.mashit.ui.screens.main.shop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.tymoshenko.mashit.data.models.mashi.MashiDetails
import dev.tymoshenko.mashit.data.models.mashi.multipleExample
import dev.tymoshenko.mashit.ui.screens.main.mashi.MashiBottomSheet
import dev.tymoshenko.mashit.ui.theme.ContentAccentColor
import dev.tymoshenko.mashit.ui.theme.ExtraSmallPaddingSize
import dev.tymoshenko.mashit.ui.theme.PaddingSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shop() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isBottomSheet by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<ShopViewModel>()
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
        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Shop", fontWeight = FontWeight.Bold, color = ContentAccentColor)
        }

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(PaddingSize)
        ) {
            item {
                ShopSection(
                    sectionName = "Ervindas",
                    onMashiClick = selectMashi,
                    sectionItems = multipleExample
                )
            }

            item {
                ShopSection(
                    sectionName = "Ervindas",
                    onMashiClick = selectMashi,
                    sectionItems = multipleExample
                )
            }

            item {
                ShopSection(
                    sectionName = "Ervindas",
                    onMashiClick = selectMashi,
                    sectionItems = multipleExample
                )
            }

            item {
                ShopSection(
                    sectionName = "Ervindas",
                    onMashiClick = selectMashi,
                    sectionItems = multipleExample
                )
            }
        }
    }

    if (isBottomSheet) {
        selectedMashi?.let {
            MashiBottomSheet(
                sheetState = sheetState,
                scope = scope,
                closeBottomShit = closeBottomShit
            )
        }
    }
}