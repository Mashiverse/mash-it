package com.mashiverse.mashit.data.remote.apis

import com.mashiverse.mashit.data.remote.dtos.MetadataDto
import retrofit2.http.GET
import retrofit2.http.Path

interface IpfsApi {

    @GET("ipfs/{uri}")
    suspend fun getMetadataByIpfsUri(
        @Path("uri") uri: String
    ): MetadataDto
}