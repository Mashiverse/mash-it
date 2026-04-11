package com.mashiverse.mashit.data.states.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.intents.ImageIntent
import com.mashiverse.mashit.utils.helpers.nft.detectImageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

private val svgCheckSemaphore = Semaphore(10)

@Composable
fun rememberImageType(
    data: String,
    processImageIntent: (ImageIntent) -> Unit
): State<ImageType?> {
    val imageType = remember(data) { mutableStateOf<ImageType?>(null) }

    if (imageType.value != null) return imageType

    LaunchedEffect(data) {
        processImageIntent(
            ImageIntent.OnTypeGet(
                url = data,
                onResult = { type ->
                    imageType.value = type
                }
            ))

        val type = withContext(Dispatchers.IO) {
            svgCheckSemaphore.withPermit {
                try {
                    val connection = (URL(data).openConnection() as HttpURLConnection).apply {
                        requestMethod = "GET"
                        connectTimeout = 5000
                        readTimeout = 5000
                    }

                    connection.inputStream.use { stream ->
                        detectImageType(stream)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    ImageType.OTHER
                }
            }
        }

        imageType.value = type
        processImageIntent(ImageIntent.OnTypeSet(url = data, type = type))
    }

    return imageType
}