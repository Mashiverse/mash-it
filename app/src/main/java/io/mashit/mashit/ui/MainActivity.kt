package io.mashit.mashit.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.reown.appkit.client.AppKit
import dagger.hilt.android.AndroidEntryPoint
import io.mashit.mashit.ui.screens.main.Main
import io.mashit.mashit.ui.theme.DarkSystemBarStyle
import io.mashit.mashit.ui.theme.MashItTheme
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppKit.register(this)

        if (Timber.treeCount == 0) {
            Timber.plant(Timber.DebugTree())
        }

        enableEdgeToEdge(
            statusBarStyle = DarkSystemBarStyle,
            navigationBarStyle = DarkSystemBarStyle
        )

        setContent {
            MashItTheme {
                Main()
            }
        }

    }
}