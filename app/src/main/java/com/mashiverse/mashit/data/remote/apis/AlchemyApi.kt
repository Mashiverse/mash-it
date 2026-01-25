package com.mashiverse.mashit.data.remote.apis

import com.mashiverse.mashit.data.remote.dtos.AlchemyDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AlchemyApi {

    @GET("nft/v3/{apiKey}/getNFTsForOwner")
    suspend fun getNFTsForOwner(
        @Path("apiKey") apiKey: String,
        @Query("owner") owner: String,
        @Query("withMetadata") withMetadata: Boolean = true,
        @Query("contractAddresses[]") contractAddress: String, // single string
        @Query("pageKey") pageKey: String? = null
    ): AlchemyDto
}