package com.mashiverse.mashit.ui.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.Secondary
import com.mashiverse.mashit.ui.theme.SmallPadding

@Composable
fun BuyButton(
    text: String,
    enabled: Boolean,
    textSize: TextUnit = 10.sp,
    onClick: () -> Unit
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            Color(165, 9, 9),
            Color(100, 0, 0)
        )
    )

    Button(
        modifier = Modifier
            .height(32.dp)
            .width(72.dp)
            .background(
                brush = if (enabled) {
                    gradient
                } else {
                    Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent))
                },
                shape = ButtonDefaults.shape
            )
            .clip(shape = ButtonDefaults.shape),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = Color.Transparent,
            disabledContainerColor = Secondary
        ),
        enabled = enabled,
        onClick = onClick,
        border = BorderStroke(
            width = (1).dp,
            color = if (enabled) {
                Color.Red
            } else {
                Color.Transparent
            }
        ),
        contentPadding = PaddingValues(horizontal = SmallPadding),
    ) {
        Text(
            text = text,
            fontSize = textSize,
            color = if (enabled) {
                ContentAccentColor
            } else {
                ContentColor
            },
            fontWeight = if (enabled) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            },
        )
    }
}
