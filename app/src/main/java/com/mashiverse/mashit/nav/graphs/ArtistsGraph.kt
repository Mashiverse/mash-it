package com.mashiverse.mashit.nav.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mashiverse.mashit.nav.routes.ArtistsRoutes
import com.mashiverse.mashit.ui.screens.artists.page.ArtistPage
import com.mashiverse.mashit.ui.screens.artists.preview.ArtistsPreview

fun NavGraphBuilder.artistsGraph(
    innerNavController: NavHostController,
) {
    composable<ArtistsRoutes.List> {
        ArtistsPreview(
            navController = innerNavController,
        )
    }

    composable<ArtistsRoutes.Page> { backStackEntry ->
        val pageRoute: ArtistsRoutes.Page = backStackEntry.toRoute()
        val alias = pageRoute.alias

        ArtistPage(
            alias = alias,
        )
    }
}