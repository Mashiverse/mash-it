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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.tymoshenko.mashit.utils.color.helpers.ColorPickerHelper.calculateInitialProgress

private const val thumbRadius = 20f

@Composable
fun ColorSlideBar(colors: List<Color>, initialColor: Color, onProgress: (Float) -> Unit) {
    var progress by remember { mutableFloatStateOf(calculateInitialProgress(initialColor, colors)) }
    var slideBarSize by remember {
        mutableStateOf(IntSize.Zero)
    }
    LaunchedEffect(progress) {
        onProgress(progress)
    }
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp) // slightly taller to give room for thumb
            .onSizeChanged {
                slideBarSize = it
            }
            .graphicsLayer {
                clip = false // 🔥 allow drawing outside bounds
            }
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        val down = awaitFirstDown() // wait for first touch

                        // Update picker on tap
                        val xPosition =
                            down.position.x.coerceIn(0f, slideBarSize.width.toFloat())
                        progress = (xPosition / slideBarSize.width).coerceIn(0f, 1f)

                        // Track drag
                        val pointerId = down.id
                        drag(pointerId) { change ->
                            val xPosition =
                                change.position.x.coerceIn(0f, slideBarSize.width.toFloat())
                            progress = (xPosition / slideBarSize.width).coerceIn(0f, 1f)
                            change.consume() // mark as handled
                        }
                    }
                }
            }
    ) {
        // BAR HEIGHT (smaller than canvas)
        val barHeight = size.height / 2
        val barTop = (size.height - barHeight) / 2

        // Gradient bar (clipped)
        drawRoundRect(
            brush = Brush.horizontalGradient(colors),
            topLeft = Offset(0f, barTop),
            size = Size(size.width, barHeight),
            cornerRadius = CornerRadius(100f, 100f)
        )

        // Thumb (can go out of bounds now)
        val thumbCenterX = thumbRadius + ((size.width - thumbRadius * 2) * progress)
        drawCircle(
            color = Color.White,
            radius = thumbRadius,
            center = Offset(thumbCenterX, size.height / 2)
        )
    }
}