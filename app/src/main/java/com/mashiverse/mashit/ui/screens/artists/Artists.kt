package com.mashiverse.mashit.ui.screens.artists

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mashiverse.mashit.nav.graphs.artistsGraph
import com.mashiverse.mashit.nav.routes.ArtistsRoutes


@Composable
fun Artists(parentNavController: NavHostController, searchQuery: State<String>) {

    val innerNavController = rememberNavController()

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = innerNavController,
        startDestination = ArtistsRoutes.List
    ) {
        artistsGraph(
            searchQuery = searchQuery,
            innerNavController = innerNavController,
            parentNavController = parentNavController
        )
    }
}