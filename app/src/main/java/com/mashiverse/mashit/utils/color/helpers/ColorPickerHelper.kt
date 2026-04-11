package com.mashiverse.mashit.utils.color.helpers

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt

fun Color.toHexString(): String {
    val r = (red * 255).toInt()
    val g = (green * 255).toInt()
    val b = (blue * 255).toInt()
    return String.format("%02X%02X%02X", r, g, b) // RRGGBB
}

fun Color.toRGB(): Triple<Int, Int, Int> {
    return Triple(
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
}

fun String.toHexColor(): Color {
    val cleanHex = this.removePrefix("#")
    return Color("#$cleanHex".toColorInt())
}

object ColorPickerHelper {

    fun Color.toHue(): Float {
        val hsv = FloatArray(3)
        android.graphics.Color.colorToHSV(this.toArgb(), hsv)
        return hsv[0]
    }

    fun hsvToColor(hue: Float, saturation: Float, value: Float): Color {
        val hsv = floatArrayOf(hue, saturation, value)
        return Color(android.graphics.Color.HSVToColor(hsv))
    }
}