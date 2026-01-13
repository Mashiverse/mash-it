package io.mashit.mashit.ui.screens.mashup

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
import io.mashit.mashit.data.models.mashi.TraitType
import io.mashit.mashit.ui.theme.ActiveMashupButtonBackground
import io.mashit.mashit.ui.theme.ContentAccentColor
import io.mashit.mashit.ui.theme.ContentColor
import io.mashit.mashit.ui.theme.ExtraSmallPaddingSize
import io.mashit.mashit.ui.theme.InactiveMashupButtonBackground
import io.mashit.mashit.ui.theme.SmallPaddingSize
import kotlinx.coroutines.Job

@Composable
fun MashupCategories(
    onMashupCategorySelect: (TraitType) -> Job,
    selectedCategory: TraitType
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(ExtraSmallPaddingSize)
    ) {
        items(TraitType.entries) { traitType ->
            Button(
                modifier = Modifier
                    .height(36.dp),
                onClick = { onMashupCategorySelect.invoke(traitType) },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = if (selectedCategory == traitType) {
                        ActiveMashupButtonBackground
                    } else {
                        InactiveMashupButtonBackground
                    },
                    contentColor = if (selectedCategory == traitType) ContentAccentColor else ContentColor
                ),
                contentPadding = PaddingValues(horizontal = SmallPaddingSize)
            ) {
                Text(
                    text = traitType.name.lowercase().replace("_", " "),
                    fontSize = 14.sp,
                )
            }
        }
    }
}