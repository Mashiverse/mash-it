package io.mashit.mashit.nav.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class MainRoutes {
    @Serializable
    data object Shop: MainRoutes()
    @Serializable
    data object Artists: MainRoutes()
    @Serializable
    data object Collection: MainRoutes()
    @Serializable
    data object Mashup: MainRoutes()
}