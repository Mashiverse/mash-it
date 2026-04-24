package com.mashiverse.mashit.ui.theme

import android.app.Activity
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun MashitTheme(content: @Composable () -> Unit) {
    val view = LocalView.current

    SideEffect {
        val window = (view.context as Activity).window
        val controller = WindowCompat.getInsetsController(window, view)
        controller.isAppearanceLightStatusBars = false
        controller.isAppearanceLightNavigationBars = false
    }

    MaterialTheme(
        colorScheme = darkColorScheme(),
        typography = Typography(),
        content = {
            CompositionLocalProvider(
                LocalTextStyle provides GeistBaseStyle,
                content = content
            )
        }
    )
}