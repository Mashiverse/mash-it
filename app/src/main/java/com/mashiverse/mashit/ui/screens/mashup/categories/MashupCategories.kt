package com.mashiverse.mashit.ui.screens.mashup.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.nft.TraitType
import com.mashiverse.mashit.ui.theme.ActiveButtonBackground
import com.mashiverse.mashit.ui.theme.ButtonBackground
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding

@Composable
fun MashupCategories(
    isCollectibles: Boolean,
    onCollectiblesSelect: () -> Unit,
    onCategorySelect: (TraitType) -> Unit,
    selectedCategory: TraitType
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(SmallPadding)
    ) {
        item {
            Button(
                modifier = Modifier
                    .height(32.dp),
                onClick = onCollectiblesSelect,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = if (isCollectibles) {
                        ActiveButtonBackground
                    } else {
                        ButtonBackground
                    },
                    contentColor = if (isCollectibles) ContentAccentColor else ContentColor
                ),
                contentPadding = PaddingValues(horizontal = Padding)
            ) {
                Text(
                    text = "Collectibles",
                    fontSize = 14.sp,
                )
            }
        }

        items(TraitType.entries) { traitType ->
            Button(
                modifier = Modifier
                    .height(32.dp),
                onClick = { onCategorySelect.invoke(traitType) },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = if (!isCollectibles && selectedCategory == traitType) {
                        ActiveButtonBackground
                    } else {
                        ButtonBackground
                    },
                    contentColor = if (!isCollectibles && selectedCategory == traitType) ContentAccentColor else ContentColor
                ),
                contentPadding = PaddingValues(horizontal = Padding)
            ) {
                Text(
                    text = traitType.name.lowercase().replace("_", " ").split(" ")
                        .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } },
                    fontSize = 14.sp,
                )
            }
        }
    }
}