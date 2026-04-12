package com.mashiverse.mashit.data.models.sys.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

data class DialogContent(
    val icon: ImageVector = Icons.Default.Notifications,
    val title: String,
    val text: String
)
