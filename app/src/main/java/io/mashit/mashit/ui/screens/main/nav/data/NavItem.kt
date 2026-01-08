package io.mashit.mashit.ui.screens.main.nav.data

import io.mashit.mashit.nav.routes.MainRoutes

data class NavItem(
    val label: String,
    val route: MainRoutes
)

val navItems = listOf(
    NavItem("Shop", MainRoutes.Shop),
    NavItem("Artists", MainRoutes.Artists),
    NavItem("Collection", MainRoutes.Collection),
    NavItem("Mashup", MainRoutes.Mashup)
)