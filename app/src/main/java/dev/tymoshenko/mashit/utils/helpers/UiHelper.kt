package dev.tymoshenko.mashit.utils.helpers

import android.app.Activity
import android.view.View
import androidx.core.view.WindowCompat

fun View.changeStatusBarColor() {
    val window = (this.context as? Activity)?.window
    if (window != null) {
        WindowCompat.getInsetsController(window, this).isAppearanceLightStatusBars = false
    }
}