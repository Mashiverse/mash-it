package dev.tymoshenko.mashit.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.tymoshenko.mashit.nav.graphs.appGraph
import dev.tymoshenko.mashit.nav.routes.AppRoutes
import dev.tymoshenko.mashit.ui.theme.Background
import dev.tymoshenko.mashit.ui.theme.DarkSystemBarStyle
import dev.tymoshenko.mashit.ui.theme.MashItTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = DarkSystemBarStyle,
            navigationBarStyle = DarkSystemBarStyle
        )
        setContent {
            MashItTheme {
                val navController = rememberNavController()

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Background)
                )

                NavHost(
                    navController = navController,
                    startDestination = AppRoutes.Main
                ) {
                    appGraph(navController)
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val overrideConfiguration = Configuration(
            newBase.resources.configuration
        ).apply { fontScale = 1.0f }

        super.attachBaseContext(
            newBase.createConfigurationContext(overrideConfiguration)
        )
    }
}