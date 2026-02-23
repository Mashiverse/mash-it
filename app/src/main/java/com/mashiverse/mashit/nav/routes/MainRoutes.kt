package com.mashiverse.mashit.nav.routes

import com.kenai.jffi.Main
import kotlinx.serialization.Serializable

@Serializable
sealed class MainRoutes {
    @Serializable
    data class Shop(val listingId: String?) : MainRoutes()

    @Serializable
    data object Artists : MainRoutes()

    @Serializable
    data object Collection : MainRoutes()

    @Serializable
    data object Mashup : MainRoutes()

    @Serializable
    data object Settings : MainRoutes()
}