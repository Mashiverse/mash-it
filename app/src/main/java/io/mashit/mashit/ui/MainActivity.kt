package io.mashit.mashit.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import io.mashit.mashit.ui.screens.Main
import io.mashit.mashit.ui.theme.Background
import io.mashit.mashit.ui.theme.DarkSystemBarStyle
import io.mashit.mashit.ui.theme.MashItTheme
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Logging
        if (Timber.treeCount == 0) {
            Timber.plant(Timber.DebugTree())
        }

        enableEdgeToEdge(
            statusBarStyle = DarkSystemBarStyle,
            navigationBarStyle = DarkSystemBarStyle
        )

        setContent {
            MashItTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Background)
                )

                Main()
            }
        }
    }
}