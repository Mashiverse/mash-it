package io.mashit.mashit.nav.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import io.mashit.mashit.nav.routes.MainRoutes
import io.mashit.mashit.ui.screens.artists.Artists
import io.mashit.mashit.ui.screens.collection.Collection
import io.mashit.mashit.ui.screens.mashup.Mashup
import io.mashit.mashit.ui.screens.shop.Shop

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    composable<MainRoutes.Shop> {
        Shop()
    }

    composable<MainRoutes.Artists> {
        Artists()
    }

    composable<MainRoutes.Collection> {
        Collection()
    }

    composable<MainRoutes.Mashup> {
        Mashup()
    }
}