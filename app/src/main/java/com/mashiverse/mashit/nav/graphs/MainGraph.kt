package com.mashiverse.mashit.nav.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mashiverse.mashit.nav.routes.MainRoutes
import com.mashiverse.mashit.ui.screens.artists.Artists
import com.mashiverse.mashit.ui.screens.collection.Collection
import com.mashiverse.mashit.ui.screens.mashup.Mashup
import com.mashiverse.mashit.ui.screens.shop.Shop

fun NavGraphBuilder.mainGraph() {
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