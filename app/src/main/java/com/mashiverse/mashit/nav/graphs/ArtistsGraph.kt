package com.mashiverse.mashit.nav.graphs

import androidx.compose.material3.Text
import androidx.compose.runtime.State
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mashiverse.mashit.nav.routes.ArtistsRoutes
import com.mashiverse.mashit.ui.screens.artists.preview.ArtistsPreview

fun NavGraphBuilder.artistsGraph(
    innerNavController: NavHostController,
    parentNavController: NavHostController,
    searchQuery: State<String>
) {
    composable<ArtistsRoutes.List> {
        ArtistsPreview(
            navController = innerNavController,
            searchQuery = searchQuery
        )
    }

    composable<ArtistsRoutes.Page> { backStackEntry ->
        val pageRoute: ArtistsRoutes.Page = backStackEntry.toRoute()
        val alias = pageRoute.alias

        Text(
            text = alias
        )
    }
}