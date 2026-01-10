package io.mashit.mashit.nav.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import io.mashit.mashit.nav.routes.MainRoutes
import io.mashit.mashit.ui.screens.main.artists.Artists
import io.mashit.mashit.ui.screens.main.collection.Collection
import io.mashit.mashit.ui.screens.main.mashup.Mashup
import io.mashit.mashit.ui.screens.main.shop.Shop

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