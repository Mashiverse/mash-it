package com.mashiverse.mashit.data.models.intents

import com.mashiverse.mashit.data.models.dialog.DialogContent

sealed class DialogIntent {
    object Clear : DialogIntent()
    data class SetContent(val content: DialogContent) : DialogIntent()
}