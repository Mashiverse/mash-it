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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //AppKit.register(this)

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