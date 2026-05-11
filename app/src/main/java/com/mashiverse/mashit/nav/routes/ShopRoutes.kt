package com.mashiverse.mashit.nav.routes

import androidx.compose.runtime.State
import kotlinx.serialization.Serializable

@Serializable
sealed class ShopRoutes {

    @Serializable
    data class RegularShop(
        val listingId: String?
    ) : ShopRoutes()

    @Serializable
    data class SpecialShop(
        val slug: String
    ) : ShopRoutes()
}