package dev.tymoshenko.mashit.ui.screens.main.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.tymoshenko.mashit.ui.theme.SearchBarBackground

@Composable
fun Shop() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            ShopSection(sectionName = "Section",(1..10).toList().map { {ShopItem(it) }})
        }
        item { ShopSection(sectionName = "Banner Section", (1..5).toList().map { {ShopBanner(it) }}) }
        item {
            ShopSection(sectionName = "Section 2", (1..10).toList().map { {ShopItem(it) }})
        }
        item {
            ShopSection(sectionName = "Section 3", (1..10).toList().map { {ShopItem(it) }})
        }
        item { ShopSection(sectionName = "Banner Section 2", (1..5).toList().map { {ShopBanner(it) }}) }
        item { ShopSection(sectionName = "Banner Section 3", (1..5).toList().map { {ShopBanner(it) }}) }

        item { ShopSection(sectionName = "Banner Section 4", (1..5).toList().map { {ShopBanner(it) }}) }
        item { ShopSection(sectionName = "Banner Section 5", (1..5).toList().map { {ShopBanner(it) }}) }
        item { ShopSection(sectionName = "Banner Section 6", (1..5).toList().map { {ShopBanner(it) }}) }
    }
}