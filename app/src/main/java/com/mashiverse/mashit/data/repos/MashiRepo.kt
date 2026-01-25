package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.models.mashi.MashupResult
import com.mashiverse.mashit.data.remote.apis.MashiApi
import okhttp3.ResponseBody
import javax.inject.Inject

class MashiRepo @Inject constructor(
    private val mashiApi: MashiApi
) {
    suspend fun getMashup(wallet: String, isStatic: Boolean = true): MashupResult {
        val responseBody: ResponseBody = mashiApi.getMashup(wallet, isStatic)

        // Read bytes from the response
        val bytes = responseBody.byteStream().use { it.readBytes() }

        // Get content type as string (e.g., "image/png" or "image/gif")
        val contentType = responseBody.contentType()?.toString() ?: "image/png"

        return MashupResult(bytes, contentType)
    }
}