package com.mashiverse.mashit.data.states.mashup

import com.mashiverse.mashit.data.models.sys.image.DownloadType

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
        val downloadType: DownloadType
    ) : ActionsIntent()
}