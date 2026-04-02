package com.mashiverse.mashit.ui.theme

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import com.mashiverse.mashit.R

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

val Cooper = FontFamily(
    Font(R.font.cooper_black_std, FontWeight.Normal)
)

val GeistBaseStyle = TextStyle(
    fontFamily = Geist,
    platformStyle = PlatformTextStyle(
        includeFontPadding = false
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.Both
    )
)