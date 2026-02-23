package com.mashiverse.mashit.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mashiverse.mashit.ui.screens.Main
import com.mashiverse.mashit.ui.theme.Background
import com.mashiverse.mashit.ui.theme.DarkSystemBarStyle
import com.mashiverse.mashit.ui.theme.MashItTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Timber.treeCount == 0) {
            Timber.plant(Timber.DebugTree())
        }

        enableEdgeToEdge(
            statusBarStyle = DarkSystemBarStyle,
            navigationBarStyle = DarkSystemBarStyle
        )

        setContent {
            navController = rememberNavController()

            MashItTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Background)
                ) {
                    Main(navController = navController)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }
}