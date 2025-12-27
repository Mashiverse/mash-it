package dev.tymoshenko.mashit.utils.color.ext

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

fun DrawScope.drawColorSelector(color: Color, location: Offset) {
    drawCircle(color, radius = 30f, center = location)
    drawCircle(Color.White, radius = 30f, center = location, style = Stroke(5f))
}