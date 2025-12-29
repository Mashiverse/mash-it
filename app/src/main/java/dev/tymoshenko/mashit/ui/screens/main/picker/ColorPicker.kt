package dev.tymoshenko.mashit.ui.screens.main.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
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
    initialColor: Color = Color.Red,
    onPickedColor: (Color) -> Unit
) {
    var pickerLocation by remember {
        mutableStateOf(Offset.Zero)
    }
    var colorPickerSize by remember {
        mutableStateOf(IntSize(1, 1))
    }

    var rangeColor by remember {
        mutableStateOf(initialColor)
    }
    var color by remember {
        mutableStateOf(initialColor)
    }

    val changeColor = { pickerLocation: Offset ->
        val xProgress =
            (1f - (pickerLocation.x / colorPickerSize.width)).coerceIn(0f, 1f)
        val yProgress =
            (pickerLocation.y / colorPickerSize.height).coerceIn(0f, 1f)
        if (xProgress.isNaN().not() && yProgress.isNaN().not()) {
            val newColor = Color(
                rangeColor
                    .red()
                    .lighten(xProgress)
                    .darken(yProgress),
                rangeColor
                    .green()
                    .lighten(xProgress)
                    .darken(yProgress),
                rangeColor
                    .blue()
                    .lighten(xProgress)
                    .darken(yProgress),
            )

            if (newColor != color) {
                color = newColor
                onPickedColor(newColor)
            }
        }
    }

    LaunchedEffect(colorPickerSize, initialColor) {
        if (initialColor == color) return@LaunchedEffect

        if (colorPickerSize.width > 1 && colorPickerSize.height > 1) {
            val hsv = FloatArray(3)
            android.graphics.Color.colorToHSV(initialColor.toArgb(), hsv)

            // 1. Set the background rangeColor to the pure Hue (Saturation=1, Value=1)
            val pureHue = android.graphics.Color.HSVToColor(floatArrayOf(hsv[0], 1f, 1f))
            rangeColor = Color(pureHue)

            // 2. Calculate coordinates based on Saturation and Value
            pickerLocation = Offset(
                x = colorPickerSize.width * hsv[1],
                y = colorPickerSize.height * (1f - hsv[2])
            )
            changeColor(pickerLocation)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .onSizeChanged { colorPickerSize = it }
                .drawWithContent {
                    drawContent()
                    drawColorSelector(color.copy(alpha = 1f), pickerLocation)
                }
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.horizontalGradient(listOf(Color.White, rangeColor)))
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
                .pointerInput(Unit) {
                    forEachGesture {
                        awaitPointerEventScope {
                            // Wait for the first touch
                            val down = awaitFirstDown()

                            // Update picker immediately (tap)
                            pickerLocation = Offset(
                                x = down.position.x.coerceIn(0f, colorPickerSize.width.toFloat()),
                                y = down.position.y.coerceIn(0f, colorPickerSize.height.toFloat())
                            )

                            changeColor.invoke(pickerLocation)


                            var pointerId = down.id
                            drag(pointerId) { change ->
                                pickerLocation = Offset(
                                    x = change.position.x.coerceIn(
                                        0f,
                                        colorPickerSize.width.toFloat()
                                    ),
                                    y = change.position.y.coerceIn(
                                        0f,
                                        colorPickerSize.height.toFloat()
                                    )
                                )

                                changeColor.invoke(pickerLocation)

                                change.consume() // mark event as handled
                            }
                        }
                    }
                }
        )

        Spacer(modifier = Modifier.height(LargePaddingSize))

        ColorSlideBar(colors = gradientColors, initialColor = color) {
            val (rangeProgress, range) = ColorPickerHelper.calculateRangeProgress(it.toDouble())
            val red: Int
            val green: Int
            val blue: Int
            when (range) {
                ColorRange.RED_TO_YELLOW -> {
                    red = 255
                    green = (255 * rangeProgress).roundToInt()
                    blue = 0
                }
                ColorRange.YELLOW_TO_GREEN -> {
                    red = (255 * (1 - rangeProgress)).roundToInt()
                    green = 255
                    blue = 0
                }
                ColorRange.GREEN_TO_CYAN -> {
                    red = 0
                    green = 255
                    blue = (255 * rangeProgress).roundToInt()
                }
                ColorRange.CYAN_TO_BLUE -> {
                    red = 0
                    green = (255 * (1 - rangeProgress)).roundToInt()
                    blue = 255
                }
                ColorRange.BLUE_TO_PURPLE -> {
                    red = (255 * rangeProgress).roundToInt()
                    green = 0
                    blue = 255
                }
                ColorRange.PURPLE_TO_RED -> {
                    red = 255
                    green = 0
                    blue = (255 * (1 - rangeProgress)).roundToInt()
                }
            }
            rangeColor = Color(red, green, blue)
            changeColor(pickerLocation)
        }
    }
}