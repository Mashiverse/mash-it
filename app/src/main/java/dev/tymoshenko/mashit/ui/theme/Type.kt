package dev.tymoshenko.mashit.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.tymoshenko.mashit.R

// Set of Material typography styles to start with

val Geist = FontFamily(
    Font(R.font.geist_regular, FontWeight.Normal),
    Font(R.font.geist_semi_bold, FontWeight.SemiBold),
    Font(R.font.geist_medium, FontWeight.Medium),
    Font(R.font.geist_bold, FontWeight.Bold),
    Font(R.font.geist_extra_bold, FontWeight.ExtraBold),
    Font(R.font.geist_extra_light, FontWeight.ExtraLight),
    Font(R.font.geist_light, FontWeight.Light),
    Font(R.font.geist_thin, FontWeight.Thin),
    Font(R.font.geist_black, FontWeight.Black)
)

private val defaultTypography = Typography()
val GeistTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = Geist),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = Geist),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = Geist),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = Geist),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = Geist),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = Geist),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = Geist),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = Geist),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = Geist),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = Geist),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = Geist),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = Geist),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = Geist),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = Geist),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = Geist)
)