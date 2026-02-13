package com.mashiverse.mashit.ui.screens.components.picker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                    if (pickerSize.width <= 0 || pickerSize.height <= 0) return@pointerInput

                    awaitEachGesture {
                        // 1. Wait for the initial touch down
                        val down = awaitFirstDown()

                        isDragging = true
                        onDraggingChange(true)

                        val updatePosition: (Offset) -> Unit = { pos ->
                            val constrained = Offset(
                                x = pos.x.coerceIn(0f, pickerSize.width.toFloat()),
                                y = pos.y.coerceIn(0f, pickerSize.height.toFloat())
                            )
                            internalLocation = constrained
                            onPickerLocationChange(constrained)

                            val saturation = (constrained.x / pickerSize.width).coerceIn(0f, 1f)
                            val brightness =
                                (1f - (constrained.y / pickerSize.height)).coerceIn(0f, 1f)

                            val newColor = ColorPickerHelper.hsvToColor(
                                rangeColor.toHue(),
                                saturation,
                                brightness
                            )
                            onPickedColor(newColor)
                        }

                        // Initial update on tap
                        updatePosition(down.position)

                        // 2. Track the drag until release or cancellation
                        drag(down.id) { change ->
                            updatePosition(change.position)
                            change.consume()
                        }

                        // 3. Cleanup state when finger is lifted or gesture is lost
                        isDragging = false
                        onDraggingChange(false)
                    }
                }
        )

        // 2️⃣ Selector circle (drawn outside clipped background)
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawColorSelector(color = color, location = internalLocation)
        }
    }
}
