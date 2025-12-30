package dev.tymoshenko.mashit.ui.screens.main.picker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

private const val thumbRadius = 20f

@Composable
fun ColorSlideBar(colors: List<Color>, color: Color, onProgress: (Float) -> Unit) {
    var progress by remember { mutableFloatStateOf(0f) }
    var slideBarSize by remember { mutableStateOf(IntSize.Zero) }

    // 🔥 Track if the user is currently touching the slider
    var isDragging by remember { mutableStateOf(false) }

    var lastHue by remember { mutableFloatStateOf(-1f) }


    LaunchedEffect(color) {
        if (!isDragging) {
            val hsv = FloatArray(3)
            android.graphics.Color.colorToHSV(color.toArgb(), hsv)

            val currentHue = hsv[0]

            if (currentHue != lastHue) {
                lastHue = currentHue
                progress = currentHue / 360f
            }
        }
    }

    LaunchedEffect(color) {
        isDragging = true
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp) // Height for the thumb area
            .onSizeChanged { slideBarSize = it }
            .graphicsLayer { clip = false }
            .pointerInput(slideBarSize) {
                forEachGesture {
                    awaitPointerEventScope {
                        val down = awaitFirstDown()
                        isDragging = true // Block external sync

                        // TrackWidth is total width minus the padding needed for thumb centers
                        val trackWidth = (slideBarSize.width - (thumbRadius * 2)).coerceAtLeast(1f)

                        val updateProgress = { xPos: Float ->
                            // Normalize touch: subtract radius so 0 starts at thumb's leftmost center
                            val relativeX = (xPos - thumbRadius).coerceIn(0f, trackWidth)
                            progress = relativeX / trackWidth
                            onProgress(progress)
                        }

                        updateProgress(down.position.x)

                        drag(down.id) { change ->
                            updateProgress(change.position.x)
                            change.consume()
                        }

                        isDragging = false // Re-enable external sync
                    }
                }
            }
    ) {
        val barHeight = 8.dp.toPx()
        val barTop = (size.height - barHeight) / 2

        // Gradient bar
        drawRoundRect(
            brush = Brush.horizontalGradient(colors),
            topLeft = Offset(0f, barTop),
            size = Size(size.width, barHeight),
            cornerRadius = CornerRadius(barHeight, barHeight)
        )

        // 🔥 Match the thumb drawing logic exactly to the pointer input logic
        val trackWidth = size.width - (thumbRadius * 2)
        val thumbCenterX = thumbRadius + (trackWidth * progress)

        // Optional: Small shadow for the thumb
        drawCircle(
            color = Color.Black.copy(alpha = 0.1f),
            radius = thumbRadius + 2f,
            center = Offset(thumbCenterX, size.height / 2)
        )

        drawCircle(
            color = Color.White,
            radius = thumbRadius,
            center = Offset(thumbCenterX, size.height / 2)
        )
    }
}

