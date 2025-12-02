package dev.tymoshenko.mashit.nav.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import dev.tymoshenko.mashit.nav.routes.MainRoutes
import dev.tymoshenko.mashit.ui.screens.main.artists.Artists
import dev.tymoshenko.mashit.ui.screens.main.collection.Collection
import dev.tymoshenko.mashit.ui.screens.main.mashup.Mashup
import dev.tymoshenko.mashit.ui.screens.main.shop.Shop

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