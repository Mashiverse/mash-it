package dev.tymoshenko.mashit.ui.screens.main.mashup.color

import android.hardware.camera2.CameraExtensionSession
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import dev.tymoshenko.mashit.ui.theme.ColorFieldBackground
import dev.tymoshenko.mashit.ui.theme.ColorPreviewSize
import dev.tymoshenko.mashit.ui.theme.ContentAccentColor
import dev.tymoshenko.mashit.ui.theme.SmallPaddingSize
import dev.tymoshenko.mashit.utils.color.helpers.toHexColor
import dev.tymoshenko.mashit.utils.color.helpers.toHexString
import dev.tymoshenko.mashit.utils.color.helpers.toRGB

@Composable
fun ColorActionsField(
    text: String,
    onTextChange: (String) -> Unit,
    width: Dp,
    height: Dp,
    filterStrategy: (String) -> String,
    isHex: Boolean = false
) {
    Box(
        modifier = Modifier
            .height(height)
            .width(width)
            .clip(shape = RoundedCornerShape(8.dp))
            .wrapContentSize(unbounded = true)
            .clipToBounds(),
        contentAlignment = Alignment.Center
    ) {
        TextField(
            value = text,
            onValueChange = { input ->
                onTextChange.invoke(filterStrategy(input))
            },
            colors = TextFieldDefaults.colors().copy(
                unfocusedContainerColor = ColorFieldBackground,
                focusedContainerColor = ColorFieldBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 14.sp,
                color = ContentAccentColor,
                textAlign = TextAlign.Center
            ),
            visualTransformation = { originalText ->
                if (isHex) {
                    TransformedText(
                        text = AnnotatedString("#" + originalText.text),
                        offsetMapping = object : OffsetMapping {
                            override fun originalToTransformed(offset: Int) = offset + 1
                            override fun transformedToOriginal(offset: Int) =
                                (offset - 1).coerceAtLeast(0)
                        }
                    )
                } else TransformedText(text = originalText, offsetMapping = OffsetMapping.Identity)
            }
        )
    }
}

@Composable
fun ColorActions(modifier: Modifier = Modifier, changePreviewColor: (Color) -> Unit, color: Color) {
    val hex = remember(color) { mutableStateOf(color.toHexString()) }
    val r = remember(color) { mutableIntStateOf(color.toRGB().first) }
    val g = remember(color) { mutableIntStateOf(color.toRGB().second) }
    val b = remember(color) { mutableIntStateOf(color.toRGB().third) }

    LaunchedEffect(r.intValue, g.intValue, b.intValue) {
        val safeR = r.intValue.coerceIn(0, 255)
        val safeG = g.intValue.coerceIn(0, 255)
        val safeB = b.intValue.coerceIn(0, 255)
        changePreviewColor(Color(safeR, safeG, safeB))
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
            Text("HEX", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ContentAccentColor)

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
            Text("RGB", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ContentAccentColor)

            Spacer(modifier = Modifier.weight(1F))

            ColorActionsField(
                text = r.intValue.toString(),
                onTextChange = { r.intValue = it.toIntOrNull() ?: 0 },
                width = rgbWidth,
                height = 32.dp,
                filterStrategy = rgbFilterStrategy
            )
            Spacer(modifier = Modifier.width(SmallPaddingSize))
            ColorActionsField(
                text = g.intValue.toString(),
                onTextChange = { g.intValue = it.toIntOrNull() ?: 0 },
                width = rgbWidth,
                height = 32.dp,
                filterStrategy = rgbFilterStrategy
            )
            Spacer(modifier = Modifier.width(SmallPaddingSize))
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