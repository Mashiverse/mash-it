package io.mashit.mashit.ui.screens.main.picker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
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
    var isDragging by remember { mutableStateOf(false) }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .graphicsLayer { clip = false }
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        val down = awaitFirstDown()
                        isDragging = true
                        val trackWidth = size.width - thumbRadius * 2
                        val update = { xPos: Float ->
                            val relative = (xPos - thumbRadius).coerceIn(0f, trackWidth)
                            onProgressChange(relative / trackWidth)
                        }
                        update(down.position.x)
                        drag(down.id) { change ->
                            update(change.position.x)
                            change.consume()
                        }
                        isDragging = false
                    }
                }
            }
    ) {
        val barHeight = 8.dp.toPx()
        val barTop = (size.height - barHeight) / 2
        drawRoundRect(
            brush = Brush.horizontalGradient(colors),
            topLeft = Offset(0f, barTop),
            size = Size(size.width, barHeight),
            cornerRadius = CornerRadius(barHeight, barHeight)
        )
        val trackWidth = size.width - thumbRadius * 2
        val thumbCenterX = thumbRadius + trackWidth * progress
        drawCircle(Color.Black.copy(alpha = 0.1f), radius = thumbRadius + 2f, center = Offset(thumbCenterX, size.height / 2))
        drawCircle(Color.White, radius = thumbRadius, center = Offset(thumbCenterX, size.height / 2))
    }
}
