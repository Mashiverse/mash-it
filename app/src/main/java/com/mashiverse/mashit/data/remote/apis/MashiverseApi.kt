package com.mashiverse.mashit.data.remote.apis

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MashiverseApi {
    @GET("/api/mashi/mashup/{wallet}")
    suspend fun getMashup(
        @Path("wallet") wallet: String,
        @Query("img_type") imgType: Int = 0,
    ): ResponseBody
}