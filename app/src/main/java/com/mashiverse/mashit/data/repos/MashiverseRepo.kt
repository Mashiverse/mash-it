package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.models.mashi.MashupResult
import com.mashiverse.mashit.data.remote.apis.MashiverseApi
import okhttp3.ResponseBody
import javax.inject.Inject

class MashiverseRepo @Inject constructor(
    private val mashiverseApi: MashiverseApi
) {
    suspend fun getMashup(wallet: String, imgType: Int = 0): MashupResult {
        val responseBody: ResponseBody = mashiverseApi.getMashup(wallet, imgType)

        // Read bytes from the response
        val bytes = responseBody.byteStream().use { it.readBytes() }

        // Get content type as string (e.g., "image/png" or "image/gif")
        val contentType = responseBody.contentType()?.toString() ?: "image/png"

        return MashupResult(bytes, contentType)
    }
}