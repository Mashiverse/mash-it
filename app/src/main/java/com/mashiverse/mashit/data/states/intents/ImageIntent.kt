package com.mashiverse.mashit.data.states.intents

import com.mashiverse.mashit.data.models.image.ImageType

sealed class ImageIntent {

    data class OnTypeGet(
        val url: String,
        val onResult: (ImageType?) -> Unit
    ) : ImageIntent()

    data class OnTypeSet(
        val url: String,
        val type: ImageType
    ) : ImageIntent()
}