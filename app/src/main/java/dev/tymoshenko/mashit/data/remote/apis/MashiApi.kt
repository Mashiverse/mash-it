package dev.tymoshenko.mashit.data.remote.apis

import dev.tymoshenko.mashit.data.remote.dtos.AlchemyDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MashiApi {
    @GET("api/get_mashup/{wallet}")
    suspend fun getMashup(
        @Path("wallet") wallet: String,
        @Query("is_static") isStatic: Boolean = true,
    ): ResponseBody
}