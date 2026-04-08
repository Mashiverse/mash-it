package com.mashiverse.mashit.data.remote.apis

import SaveMashupRes
import com.mashiverse.mashit.data.models.mashup.save.SaveMashupReq
import com.mashiverse.mashit.data.remote.dtos.HealthDto
import com.mashiverse.mashit.data.remote.dtos.listings.ListingDto
import com.mashiverse.mashit.data.remote.dtos.listings.ListingsDto
import com.mashiverse.mashit.data.remote.dtos.MashupDto
import com.mashiverse.mashit.data.remote.dtos.artists.ArtistPageDto
import com.mashiverse.mashit.data.remote.dtos.artists.ArtistListingsDto
import com.mashiverse.mashit.data.remote.dtos.artists.ArtistsDto
import com.mashiverse.mashit.data.remote.dtos.listings.SearchDto
import com.mashiverse.mashit.utils.MASHIT_KEY
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MashitApi {
    @GET("api/v1/mashers/shop")
    suspend fun getShopList(
        @Query("apiKey") apiKey: String = MASHIT_KEY,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
    ): ListingsDto

    @GET("api/v1/listings/{id}")
    suspend fun getShopItem(
        @Path("id") id: String,
        @Query("apiKey") apiKey: String = MASHIT_KEY,
    ): ListingDto

    @GET("api/v1/search")
    suspend fun getSearchList(
        @Query("q") q: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("apiKey") apiKey: String = MASHIT_KEY
    ): SearchDto

    @GET("api/v1/artists")
    suspend fun getArtists(
        @Query("apiKey") apiKey: String = MASHIT_KEY,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): ArtistsDto

    @GET("api/v1/artists/{alias}")
    suspend fun getArtistPage(
        @Path("alias") alias: String,
        @Query("apiKey") apiKey: String = MASHIT_KEY
    ): ArtistPageDto

    @GET("api/v1/artists/{alias}/listings")
    suspend fun getArtistList(
        @Path("alias") alias: String,
        @Query("apiKey") apiKey: String = MASHIT_KEY,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): ArtistListingsDto

    @GET("api/v1/health")
    suspend fun getStatus(): HealthDto

    @GET("api/mashers/latest")
    suspend fun getMashup(
        @Query("wallet") wallet: String
    ): MashupDto

    @POST("api/v1/mashers/mashups")
    suspend fun saveMashup(
        @Query("apiKey") apiKey: String = MASHIT_KEY,
        @Body request: SaveMashupReq
    ): SaveMashupRes
}