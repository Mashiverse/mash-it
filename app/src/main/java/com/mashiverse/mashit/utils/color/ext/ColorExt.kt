package com.mashiverse.mashit.utils.color.ext

import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt


fun Int.lighten(lightness: Float): Int {
    val newValue = (this + (255 - this) * lightness).coerceIn(0f, 255f)
    return newValue.roundToInt()
}

fun Int.darken(darkness: Float): Int {
    return (this - this * darkness).roundToInt()
}

fun Float.lighten(factor: Float): Float {
    return this + (1f - this) * factor
}

// Darken a single channel (0..1) by factor (0..1)
fun Float.darken(factor: Float): Float {
    return this * (1f - factor)
}

// Convenience extensions for Color channels
fun Float.clamp01(): Float = min(1f, max(0f, this))

fun Color.red(): Float {
    return this.red.clamp01()
}

fun Color.green(): Float {
    return this.green.clamp01()
}

fun Color.blue(): Float {
    return this.blue.clamp01()
}

fun Float.lighten(factor: Float, clamp: Boolean = true): Float {
    val value = this + (1f - this) * factor
    return if (clamp) value.clamp01() else value
}

fun Float.darken(factor: Float, clamp: Boolean = true): Float {
    val value = this * (1f - factor)
    return if (clamp) value.clamp01() else value
}