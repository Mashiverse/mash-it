package com.mashiverse.mashit.data.states.intents

import android.content.Context
import com.mashiverse.mashit.data.models.image.ImageType

sealed class ImageIntent {
    data class GetImageType(
        val url: String,
        val onResult: (ImageType?) -> Unit
    ) : ImageIntent()

    data class SetImageType(
        val url: String,
        val imageType: ImageType
    ) : ImageIntent()
}