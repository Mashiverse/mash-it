package com.mashiverse.mashit.ui.screens.picker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.utils.color.ext.drawColorSelector
import com.mashiverse.mashit.utils.color.helpers.ColorPickerHelper
import com.mashiverse.mashit.utils.color.helpers.ColorPickerHelper.toHue

@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    color: Color,
    rangeColor: Color,
    pickerLocation: Offset,
    onPickedColor: (Color) -> Unit,
    onPickerLocationChange: (Offset) -> Unit,
    onDraggingChange: (Boolean) -> Unit,
    onPickerSizeChange: (IntSize) -> Unit
) {
    var internalLocation by remember { mutableStateOf(pickerLocation) }
    var isDragging by remember { mutableStateOf(false) }
    var pickerSize by remember { mutableStateOf(IntSize(1, 1)) }

    // Always update picker thumb when color changes (if not dragging)
    LaunchedEffect(color, rangeColor, pickerSize) {
        if (!isDragging && pickerSize.width > 1 && pickerSize.height > 1) {
            val hsv = FloatArray(3)
            android.graphics.Color.colorToHSV(color.toArgb(), hsv)

            // thumb position
            internalLocation = Offset(
                x = hsv[1] * pickerSize.width,
                y = (1f - hsv[2]) * pickerSize.height
            )
        }
    }

    Box(modifier = modifier) {
        // 1️⃣ Background gradient (clipped)
        Box(
            modifier = modifier
                .onSizeChanged {
                    pickerSize = it
                    onPickerSizeChange(it)
                }
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.horizontalGradient(listOf(Color.White, rangeColor)))
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
                .pointerInput(pickerSize) {
                    forEachGesture {
                        awaitPointerEventScope {
                            val down = awaitFirstDown()
                            onDraggingChange(true)
                            isDragging = true

                            val updatePosition: (Offset) -> Unit = { pos ->
                                val constrained = Offset(
                                    x = pos.x.coerceIn(0f, pickerSize.width.toFloat()),
                                    y = pos.y.coerceIn(0f, pickerSize.height.toFloat())
                                )
                                internalLocation = constrained
                                onPickerLocationChange(constrained)

                                val saturation = (constrained.x / pickerSize.width).coerceIn(0f, 1f)
                                val brightness = (1f - constrained.y / pickerSize.height).coerceIn(0f, 1f)
                                val newColor = ColorPickerHelper.hsvToColor(
                                    rangeColor.toHue(),
                                    saturation,
                                    brightness
                                )
                                onPickedColor(newColor)
                            }

                            updatePosition(down.position)

                            drag(down.id) { change ->
                                updatePosition(change.position)
                                change.consume()
                            }

                            onDraggingChange(false)
                            isDragging = false
                        }
                    }
                }
        )

        // 2️⃣ Selector circle (drawn outside clipped background)
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawColorSelector(color = color, location = internalLocation)
        }
    }
}
