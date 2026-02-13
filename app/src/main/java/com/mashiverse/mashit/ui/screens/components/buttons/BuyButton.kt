package com.mashiverse.mashit.ui.screens.components.buttons

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.ui.theme.ContainerColor
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.SmallPaddingSize

@Composable
fun BuyButton(
    text: String,
    enabled: Boolean,
    height: Dp = 32.dp,
    width: Dp = 72.dp,
    textSize: TextUnit = 12.sp,
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
            .height(height)
            .width(width)
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
            disabledContainerColor = ContainerColor
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

        contentPadding = PaddingValues(horizontal = SmallPaddingSize),
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
