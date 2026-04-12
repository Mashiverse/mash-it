package com.mashiverse.mashit.data.states.sys

import com.mashiverse.mashit.data.models.sys.dialog.DialogContent

sealed class DialogIntent {

    object OnClear : DialogIntent()

    data class OnChange(val content: DialogContent) : DialogIntent()
}