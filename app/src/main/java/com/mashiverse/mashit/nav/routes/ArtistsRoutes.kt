package com.mashiverse.mashit.nav.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class ArtistsRoutes {

    @Serializable
    data object List : ArtistsRoutes()

    @Serializable
    data class Page(
        val alias: String
    ) : ArtistsRoutes()
}