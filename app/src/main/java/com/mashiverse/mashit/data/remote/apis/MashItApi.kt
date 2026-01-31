package com.mashiverse.mashit.data.remote.apis

import com.mashiverse.mashit.BuildConfig
import com.mashiverse.mashit.data.remote.dtos.ArtistsDto
import com.mashiverse.mashit.data.remote.dtos.HealthDto
import com.mashiverse.mashit.data.remote.dtos.ListingDto
import com.mashiverse.mashit.data.remote.dtos.ListingsDto
import com.mashiverse.mashit.data.remote.dtos.MashupDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MashItApi {
    @GET("api/v1/mashers/shop")
    suspend fun getShopList(
        @Query("apiKey") apiKey: String = BuildConfig.MASH_IT_API_KEY,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int = 0,
    ): ListingsDto

    @GET("api/v1/listings/{id}")
    suspend fun getShopItem(
        @Path("id") id: String,
        @Query("apiKey") apiKey: String = BuildConfig.MASH_IT_API_KEY,
    ): ListingDto

    @GET("api/v1/artists")
    suspend fun getArtists(
        @Query("apiKey") apiKey: String = BuildConfig.MASH_IT_API_KEY,
    ): ArtistsDto

    @GET("api/v1/health")
    suspend fun getStatus(): HealthDto

    @GET("api/mashers/latest")
    suspend fun getMashup(
        @Query("wallet") wallet: String
    ): MashupDto
}