package com.mashiverse.mashit.utils.color.ext

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

fun DrawScope.drawColorSelector(color: Color, location: Offset) {
    // Draw the inner filled circle for the selected color
    drawCircle(
        color = color,
        radius = 30f,
        center = location
    )
    // Draw the white border/stroke around it
    drawCircle(
        color = Color.White,
        radius = 30f,
        center = location,
        style = Stroke(width = 5f)
    )
}