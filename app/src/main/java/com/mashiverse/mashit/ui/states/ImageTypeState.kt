package com.mashiverse.mashit.ui.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.intents.ImageIntent
import com.mashiverse.mashit.utils.helpers.ImageHelper
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
            ImageIntent.GetImageType(
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
                        ImageHelper.detectImageType(stream)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    ImageType.OTHER
                }
            }
        }

        imageType.value = type
        processImageIntent(ImageIntent.SetImageType(url = data, imageType = type))
    }

    return imageType
}