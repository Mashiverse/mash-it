package dev.tymoshenko.mashit.ui.screens.main.shop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.tymoshenko.mashit.ui.screens.main.mashi.MashiBottomSheet
import dev.tymoshenko.mashit.ui.theme.PaddingSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shop() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isBottomSheet by remember { mutableStateOf(false) }

    val closeBottomShit = {
        isBottomSheet = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(onClick = {isBottomSheet = true}) { Text("Sheet") }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingSize),
            verticalArrangement = Arrangement.spacedBy(PaddingSize)
        ) {
            item {
                ShopSection(sectionName = "Section", (1..10).toList().map { { ShopItem(it, isSoldOut = false) } })
            }

            item {
                ShopSection(sectionName = "Section 2", (1..10).toList().map { { ShopItem(it, isSoldOut = true) } })
            }
            item {
                ShopSection(sectionName = "Section 3", (1..10).toList().map { { ShopItem(it, isSoldOut = false) } })
            }
            item {
                ShopSection(
                    sectionName = "Banner Section",
                    (1..5).toList().map { { ShopBanner(it) } })
            }
            item {
                ShopSection(
                    sectionName = "Banner Section 2",
                    (1..5).toList().map { { ShopBanner(it) } })
            }
            item {
                ShopSection(
                    sectionName = "Banner Section 3",
                    (1..5).toList().map { { ShopBanner(it) } })
            }

            item {
                ShopSection(
                    sectionName = "Banner Section 4",
                    (1..5).toList().map { { ShopBanner(it) } })
            }
            item {
                ShopSection(
                    sectionName = "Banner Section 5",
                    (1..5).toList().map { { ShopBanner(it) } })
            }
            item {
                ShopSection(
                    sectionName = "Banner Section 6",
                    (1..5).toList().map { { ShopBanner(it) } })
            }
        }
    }

    if (isBottomSheet) {
        MashiBottomSheet(
            sheetState = sheetState,
            scope = scope,
            closeBottomShit = closeBottomShit
        )
    }
}