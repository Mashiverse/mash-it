package com.mashiverse.mashit.ui.screens.mashup

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
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.ui.theme.ActiveMashupButtonBackground
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.InactiveMashupButtonBackground
import com.mashiverse.mashit.ui.theme.PaddingSize
import com.mashiverse.mashit.ui.theme.SmallPaddingSize
import kotlinx.coroutines.Job

@Composable
fun MashupCategories(
    onMashupCategorySelect: (TraitType) -> Job,
    selectedCategory: TraitType
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(SmallPaddingSize)
    ) {
        items(TraitType.entries) { traitType ->
            Button(
                modifier = Modifier
                    .height(32.dp),
                onClick = { onMashupCategorySelect.invoke(traitType) },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = if (selectedCategory == traitType) {
                        ActiveMashupButtonBackground
                    } else {
                        InactiveMashupButtonBackground
                    },
                    contentColor = if (selectedCategory == traitType) ContentAccentColor else ContentColor
                ),
                contentPadding = PaddingValues(horizontal = PaddingSize)
            ) {
                Text(
                    text = traitType.name.lowercase().replace("_", " ").split(" ").joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() }},
                    fontSize = 14.sp,
                )
            }
        }
    }
}