package dev.tymoshenko.mashit.ui.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.tymoshenko.mashit.nav.graphs.mainGraph
import dev.tymoshenko.mashit.nav.routes.MainRoutes
import dev.tymoshenko.mashit.ui.screens.main.components.nav.BottomNavBar
import dev.tymoshenko.mashit.ui.screens.main.components.nav.TopNavBar

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