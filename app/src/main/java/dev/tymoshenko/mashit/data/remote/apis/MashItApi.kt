package dev.tymoshenko.mashit.data.remote.apis

import dev.tymoshenko.mashit.data.remote.dtos.AlchemyDto
import dev.tymoshenko.mashit.data.remote.dtos.ListingDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MashItApi {
    @GET("api/v1/listings")
    suspend fun getShopList(
        @Path("apiKey") apiKey: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int = 0,
    ): ListingDto
}