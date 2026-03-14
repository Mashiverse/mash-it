package com.mashiverse.mashit.data.models.intents

import android.content.Context

sealed class MashupIntent {


    object OnColor : MashupIntent()
    object OnColorDismiss : MashupIntent()
    object OnRandom : MashupIntent()
    object OnSave : MashupIntent()
    object OnReset : MashupIntent()
    object OnUndo : MashupIntent()
    object OnRedo : MashupIntent()
    object OnPreview : MashupIntent()
    object OnPreviewDismiss : MashupIntent()
    data class OnGifSave(val context: Context) : MashupIntent()
    data class OnPngSave(val context: Context) : MashupIntent()
}