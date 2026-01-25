package com.mashiverse.mashit.ui.screens.nav.data

import com.mashiverse.mashit.nav.routes.MainRoutes

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