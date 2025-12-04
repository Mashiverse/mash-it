package dev.tymoshenko.mashit.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.captionBarPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.tymoshenko.mashit.nav.graphs.mainGraph
import dev.tymoshenko.mashit.nav.routes.MainRoutes
import dev.tymoshenko.mashit.ui.screens.main.components.BottomNavBar
import dev.tymoshenko.mashit.ui.screens.main.components.TopNavBar

@Composable
fun Main() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = { BottomNavBar(navController) },
        topBar = { TopNavBar() }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = MainRoutes.Shop,
            modifier = Modifier
                .padding(paddingValues)
        ) {
            mainGraph(navController)
        }
    }
}