package com.mashiverse.mashit.ui.screens.mashup.color.actions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.ui.theme.ColorPreviewSize
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.utils.color.helpers.toHexColor
import com.mashiverse.mashit.utils.color.helpers.toHexString
import com.mashiverse.mashit.utils.color.helpers.toRGB

@Composable
fun ColorActions(modifier: Modifier = Modifier, changePreviewColor: (Color) -> Unit, color: Color) {
    val r = remember { mutableIntStateOf(color.toRGB().first) }
    val g = remember { mutableIntStateOf(color.toRGB().second) }
    val b = remember { mutableIntStateOf(color.toRGB().third) }
    val hex = remember { mutableStateOf(color.toHexString()) }

    LaunchedEffect(r.intValue, g.intValue, b.intValue) {
        val safeR = r.intValue.coerceIn(0, 255)
        val safeG = g.intValue.coerceIn(0, 255)
        val safeB = b.intValue.coerceIn(0, 255)
        changePreviewColor(Color(safeR, safeG, safeB))
    }

    LaunchedEffect(color) {
        val (rr, gg, bb) = color.toRGB()
        r.intValue = rr
        g.intValue = gg
        b.intValue = bb
        hex.value = color.toHexString()
    }

    LaunchedEffect(hex.value) {
        if (hex.value.length == 6) {
            changePreviewColor(hex.value.toHexColor())
        }
    }

    val rgbWidth = 40.dp
    val hexFilterStrategy: (String) -> String = { input ->
        input.filter { it.isDigit() || it.lowercaseChar() in 'a'..'f' }.take(6)
    }
    val rgbFilterStrategy: (String) -> String = { input ->
        input.filter { it.isDigit() }.take(3)
    }

    Column(
        modifier = modifier.height(ColorPreviewSize)
    ) {
        Spacer(modifier = Modifier.weight(1F))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "HEX",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ContentAccentColor
            )

            Spacer(modifier = Modifier.weight(1F))

            ColorActionsField(
                text = hex.value,
                onTextChange = { hex.value = it },
                width = (40.dp) * 3 + 16.dp,
                height = 32.dp,
                filterStrategy = hexFilterStrategy,
                isHex = true
            )
        }

        Spacer(modifier = Modifier.weight(1F))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "RGB",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ContentAccentColor
            )

            Spacer(modifier = Modifier.weight(1F))

            ColorActionsField(
                text = r.intValue.toString(),
                onTextChange = { r.intValue = it.toIntOrNull() ?: 0 },
                width = rgbWidth,
                height = 32.dp,
                filterStrategy = rgbFilterStrategy
            )

            Spacer(modifier = Modifier.width(SmallPadding))

            ColorActionsField(
                text = g.intValue.toString(),
                onTextChange = { g.intValue = it.toIntOrNull() ?: 0 },
                width = rgbWidth,
                height = 32.dp,
                filterStrategy = rgbFilterStrategy
            )

            Spacer(modifier = Modifier.width(SmallPadding))

            ColorActionsField(
                text = b.intValue.toString(),
                onTextChange = { b.intValue = it.toIntOrNull() ?: 0 },
                width = rgbWidth,
                height = 32.dp,
                filterStrategy = rgbFilterStrategy
            )
        }

        Spacer(modifier = Modifier.weight(1F))
    }
}