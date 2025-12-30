package dev.tymoshenko.mashit.ui.screens.main.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.tymoshenko.mashit.data.models.color.ColorType
import dev.tymoshenko.mashit.utils.color.data.ColorRange
import dev.tymoshenko.mashit.utils.color.data.Colors.gradientColors
import dev.tymoshenko.mashit.ui.theme.LargePaddingSize
import dev.tymoshenko.mashit.utils.color.ext.blue
import dev.tymoshenko.mashit.utils.color.ext.darken
import dev.tymoshenko.mashit.utils.color.ext.drawColorSelector
import dev.tymoshenko.mashit.utils.color.ext.green
import dev.tymoshenko.mashit.utils.color.ext.lighten
import dev.tymoshenko.mashit.utils.color.ext.red
import dev.tymoshenko.mashit.utils.color.helpers.ColorPickerHelper
import kotlin.math.roundToInt

@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    onPickedColor: (Color) -> Unit,
) {
    var pickerLocation by remember { mutableStateOf(Offset.Zero) }
    var colorPickerSize by remember { mutableStateOf(IntSize(1, 1)) }
    var rangeColor by remember { mutableStateOf(color) }

    var isDragging by remember { mutableStateOf(false) }

    LaunchedEffect(color, colorPickerSize) {
        // 🔥 2. Only sync if NOT dragging
        if (!isDragging && colorPickerSize.width > 1) {
            val hsv = FloatArray(3)
            android.graphics.Color.colorToHSV(color.toArgb(), hsv)

            rangeColor = Color(android.graphics.Color.HSVToColor(floatArrayOf(hsv[0], 1f, 1f)))

            pickerLocation = Offset(
                x = (hsv[1]) * colorPickerSize.width,
                y = (1F - hsv[2]) * colorPickerSize.height
            )
        }
    }

    val updateFinalColor = { position: Offset, hue: Color ->
        // Note: Your math here uses (1f - xProgress) for Lighten.
        // Ensure this matches your Background Brush (White -> Hue)
        val xProgress = (1f - (position.x / colorPickerSize.width)).coerceIn(0f, 1f)
        val yProgress = (position.y / colorPickerSize.height).coerceIn(0f, 1f)

        if (!xProgress.isNaN() && !yProgress.isNaN()) {
            val newColor = Color(
                red = hue.red().lighten(xProgress).darken(yProgress),
                green = hue.green().lighten(xProgress).darken(yProgress),
                blue = hue.blue().lighten(xProgress).darken(yProgress),
            )
            onPickedColor(newColor)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .onSizeChanged { colorPickerSize = it }
                .drawWithContent {
                    drawContent()
                    // Draw selector based on external state
                    drawColorSelector(color.copy(alpha = 1f), pickerLocation)
                }
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.horizontalGradient(listOf(Color.White, rangeColor)))
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
                .pointerInput(colorPickerSize) {
                    forEachGesture {
                        awaitPointerEventScope {
                            val down = awaitFirstDown()
                            isDragging = true // Block LaunchedEffect

                            val update = { pos: Offset ->
                                val constrainedPos = Offset(
                                    x = pos.x.coerceIn(0f, colorPickerSize.width.toFloat()),
                                    y = pos.y.coerceIn(0f, colorPickerSize.height.toFloat())
                                )
                                pickerLocation = constrainedPos
                                updateFinalColor(constrainedPos, rangeColor)
                            }

                            update(down.position)

                            drag(down.id) { change ->
                                update(change.position)
                                change.consume()
                            }

                            isDragging = false // Allow LaunchedEffect again
                        }
                    }
                }
        )

        Spacer(modifier = Modifier.height(LargePaddingSize))

        ColorSlideBar(colors = gradientColors, color = color) { progress ->
            val newHue = calculateHueFromProgress(progress)
            rangeColor = newHue
            updateFinalColor(pickerLocation, newHue)
        }
    }
}

fun calculateHueFromProgress(progress: Float): Color {
    val hue = progress * 360f
    val colorInt = android.graphics.Color.HSVToColor(floatArrayOf(hue, 1f, 1f))
    return Color(colorInt)
}