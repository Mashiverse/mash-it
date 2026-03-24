package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.models.mashup.MashupResult
import com.mashiverse.mashit.data.remote.apis.MashiverseApi
import okhttp3.ResponseBody
import javax.inject.Inject

class MashiverseRepo @Inject constructor(
    private val mashiverseApi: MashiverseApi
) {
    suspend fun getMashup(wallet: String, imgType: Int = 0): MashupResult {
        val responseBody: ResponseBody = mashiverseApi.getMashup(wallet, imgType)

        val contentType = responseBody.contentType()?.toString() ?: "image/png"
        val bytes = responseBody.byteStream().use { it.readBytes() }
        return MashupResult(bytes, contentType)
    }
}