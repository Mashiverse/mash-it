package com.mashiverse.mashit.ui.screens.components.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mashiverse.mashit.data.models.dialog.DialogContent

// TODO: rework error dialog
@Composable
fun Dialog(
    dialogContent: DialogContent,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        icon = {
            Icon(imageVector = dialogContent.icon, contentDescription = null)
        },
        title = {
            Text(text = dialogContent.title)
        },
        text = {
            Text(text = dialogContent.text)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Close")
            }
        },
        confirmButton = { }
    )
}