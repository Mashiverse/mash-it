package com.mashiverse.mashit.data.intents

import com.mashiverse.mashit.data.models.dialog.DialogContent

sealed class DialogIntent {

    object OnClear : DialogIntent()

    data class OnChange(val content: DialogContent) : DialogIntent()
}