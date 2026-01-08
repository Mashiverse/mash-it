package io.mashit.mashit.ui.screens.main.mashup.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import io.mashit.mashit.ui.theme.ColorPreviewShape
import io.mashit.mashit.ui.theme.ColorPreviewSize
import io.mashit.mashit.ui.theme.PaddingSize

@Composable
fun ColorPreviewSection(
    initialColor: Color,
    updatedColor: Color
) {
    Row {
        Box(
            modifier = Modifier
                .size(ColorPreviewSize)
                .clip(ColorPreviewShape)
                .background(updatedColor)
        )

        Spacer(modifier = Modifier.width(PaddingSize))

        Box(
            modifier = Modifier
                .size(ColorPreviewSize)
                .clip(ColorPreviewShape)
                .background(initialColor)
        )
    }
}