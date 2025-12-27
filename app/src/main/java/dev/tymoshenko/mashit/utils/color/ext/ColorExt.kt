package dev.tymoshenko.mashit.utils.color.ext

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.math.roundToInt



fun Color.red(): Int {
    return toArgb() shr 16 and 0xff
}

fun Color.green(): Int {
    return toArgb() shr 8 and 0xff
}

fun Color.blue(): Int {
    return toArgb() and 0xff
}

fun Int.lighten(lightness: Float): Int {
    val newValue = (this + (255 - this) * lightness).coerceIn(0f, 255f)
    return newValue.roundToInt()
}

fun Int.darken(darkness: Float): Int {
    return (this - this * darkness).roundToInt()
}