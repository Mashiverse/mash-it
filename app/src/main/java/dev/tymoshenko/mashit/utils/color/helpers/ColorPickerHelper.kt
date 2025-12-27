package dev.tymoshenko.mashit.utils.color.helpers

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import dev.tymoshenko.mashit.data.models.color.ColorRange
import kotlin.math.pow
import kotlin.math.sqrt


fun Color.toHexString(): String {
    val red = (red * 255).toInt()
    val green = (green * 255).toInt()
    val blue = (blue * 255).toInt()

    return String.format("%02X%02X%02X", red, green, blue)             // RRGGBB
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


    fun calculateRangeProgress(progress: Double): Pair<Double, ColorRange> {
        val range: ColorRange
        return progress * 6 - when {
            progress < 1f / 6 -> {
                range = ColorRange.RED_TO_YELLOW
                0
            }

            progress < 2f / 6 -> {
                range = ColorRange.YELLOW_TO_GREEN
                1
            }

            progress < 3f / 6 -> {
                range = ColorRange.GREEN_TO_CYAN
                2
            }

            progress < 4f / 6 -> {
                range = ColorRange.CYAN_TO_BLUE
                3
            }

            progress < 5f / 6 -> {
                range = ColorRange.BLUE_TO_PURPLE
                4
            }

            else -> {
                range = ColorRange.PURPLE_TO_RED
                5
            }
        } to range
    }

    fun Color.lightness(): Float {
        return (red + green + blue) / 3f
    }

    fun Color.darkness(): Float {
        return 1f - lightness()
    }

    fun calculateInitialProgress(initialColor: Color, colors: List<Color>): Float {
        if (colors.isEmpty() || colors.size == 1) return 0f

        var bestMatchIndex = 0
        var minDistance = Float.MAX_VALUE
        for (i in 0 until colors.size - 1) {
            val distance = colorDistance(initialColor, colors[i])
            if (distance < minDistance) {
                minDistance = distance
                bestMatchIndex = i
            }
        }

        val startColor = colors[bestMatchIndex]
        val endColor = colors[bestMatchIndex + 1]
        val rangeDistance = colorDistance(startColor, endColor)
        val progressWithinRange =
            (colorDistance(startColor, initialColor) / rangeDistance).coerceIn(0f, 1f)

        return (bestMatchIndex + progressWithinRange) / (colors.size - 1)
    }

    private fun colorDistance(color1: Color, color2: Color): Float {
        return sqrt(
            (color1.red - color2.red).pow(2) +
                    (color1.green - color2.green).pow(2) +
                    (color1.blue - color2.blue).pow(2)
        )
    }
}