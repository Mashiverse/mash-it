package dev.tymoshenko.mashit.data.models

import dev.tymoshenko.mashit.nav.routes.MainRoutes

data class NavItem(
    val label: String,
    val route: MainRoutes
)

val navItems = listOf<NavItem>(
    NavItem("Shop", MainRoutes.Shop),
    NavItem("Artists", MainRoutes.Artists),
    NavItem("Collection", MainRoutes.Collection),
    NavItem("Mashup", MainRoutes.Mashup)
)