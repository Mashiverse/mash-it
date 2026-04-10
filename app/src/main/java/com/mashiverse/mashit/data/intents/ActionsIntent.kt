package com.mashiverse.mashit.data.intents

import android.content.Context
import com.mashiverse.mashit.data.models.image.DownloadType

sealed class ActionsIntent {

    object OnColor : ActionsIntent()

    object OnColorDismiss : ActionsIntent()

    object OnRandom : ActionsIntent()

    object OnSave : ActionsIntent()

    object OnReset : ActionsIntent()

    object OnUndo : ActionsIntent()

    object OnRedo : ActionsIntent()

    object OnPreview : ActionsIntent()

    object OnPreviewDismiss : ActionsIntent()

    data class OnImageSave(
        val context: Context,
        val downloadType: DownloadType
    ) : ActionsIntent()
}