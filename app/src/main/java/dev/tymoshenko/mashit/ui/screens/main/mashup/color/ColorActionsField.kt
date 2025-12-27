package dev.tymoshenko.mashit.ui.screens.main.mashup.color

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.ui.theme.ColorFieldBackground
import dev.tymoshenko.mashit.ui.theme.ContentAccentColor

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