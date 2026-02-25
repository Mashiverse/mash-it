package com.mashiverse.mashit.data.models.nav

import com.mashiverse.mashit.nav.routes.MainRoutes
import kotlinx.serialization.Serializable

@Serializable
data class NavItem(
    val label: String,
    val route: MainRoutes
)

val navItems = listOf(
    NavItem("Shop", MainRoutes.Shop(listingId = null)),
    //NavItem("Artists", MainRoutes.Artists),
    NavItem("Collection", MainRoutes.Collection),
    NavItem("Mashup", MainRoutes.Mashup)
)