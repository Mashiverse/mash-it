package dev.tymoshenko.mashit.data.remote.apis

import dev.tymoshenko.mashit.data.remote.dtos.AlchemyDto
import dev.tymoshenko.mashit.data.remote.dtos.ArtistsDto
import dev.tymoshenko.mashit.data.remote.dtos.HealthDto
import dev.tymoshenko.mashit.data.remote.dtos.ListingDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MashItApi {
    @GET("api/v1/listings")
    suspend fun getShopList(
        @Query("apiKey") apiKey: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int = 0,
    ): ListingDto

    @GET("api/v1/listings/{id}")
    suspend fun getShopItem(
        @Path("id") id: String,
        @Query("apiKey") apiKey: String,
    ): ListingDto

    @GET("api/v1/artists")
    suspend fun getArtists(
        @Query("apiKey") apiKey: String,
    ): ArtistsDto

    @GET("api/v1/health")
    suspend fun getStatus(): HealthDto
}