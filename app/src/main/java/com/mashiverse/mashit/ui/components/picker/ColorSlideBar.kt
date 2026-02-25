package com.mashiverse.mashit.ui.components.picker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

private const val thumbRadius = 20f

@Composable
fun ColorSlideBar(
    colors: List<Color>,
    progress: Float,
    onProgressChange: (Float) -> Unit
) {

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .graphicsLayer { clip = false }
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown()

                    val trackWidth = size.width - (_root_ide_package_.com.mashiverse.mashit.ui.components.picker.thumbRadius * 2)

                    // Helper to calculate progress based on X coordinate
                    val updateProgress = { xPos: Float ->
                        val relative = (xPos - _root_ide_package_.com.mashiverse.mashit.ui.components.picker.thumbRadius).coerceIn(0f, trackWidth)
                        onProgressChange(relative / trackWidth)
                    }

                    // Update immediately on tap
                    updateProgress(down.position.x)

                    // 2. Track the drag until finger is lifted or cancelled
                    drag(down.id) { change ->
                        updateProgress(change.position.x)
                        // Important: consume the event so parents (like Nav drawers) don't steal it
                        change.consume()
                    }
                }
            }
    ) {
        val barHeight = 8.dp.toPx()
        val barTop = (size.height - barHeight) / 2

        // Draw the background track
        drawRoundRect(
            brush = Brush.horizontalGradient(colors),
            topLeft = Offset(0f, barTop),
            size = Size(size.width, barHeight),
            cornerRadius = CornerRadius(barHeight / 2, barHeight / 2)
        )

        val trackWidth = size.width - (_root_ide_package_.com.mashiverse.mashit.ui.components.picker.thumbRadius * 2)
        val thumbCenterX = _root_ide_package_.com.mashiverse.mashit.ui.components.picker.thumbRadius + (trackWidth * progress)
        val centerY = size.height / 2

        // Main Thumb
        drawCircle(
            color = Color.White,
            radius = _root_ide_package_.com.mashiverse.mashit.ui.components.picker.thumbRadius,
            center = Offset(thumbCenterX, centerY)
        )
    }
}