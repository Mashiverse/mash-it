package dev.tymoshenko.mashit.ui.screens.main.shop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShopSection(
    sectionName: String,
    sectionItems: List<@Composable () -> Unit>
) {
    Column{
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(sectionName)
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sectionItems.size) { index ->
                sectionItems[index].invoke()
            }
        }
    }
}