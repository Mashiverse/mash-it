package dev.tymoshenko.mashit.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.tymoshenko.mashit.nav.graphs.appGraph
import dev.tymoshenko.mashit.nav.routes.AppRoutes
import dev.tymoshenko.mashit.ui.theme.Background
import dev.tymoshenko.mashit.ui.theme.MashItTheme
import dev.tymoshenko.mashit.utils.helpers.changeStatusBarColor

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MashItTheme {
                LocalView.current.changeStatusBarColor()
                val navController = rememberNavController()

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Background)
                )

                NavHost(
                    navController = navController,
                    startDestination = AppRoutes.Auth
                ) {
                    appGraph(navController)
                }
            }
        }
    }
}