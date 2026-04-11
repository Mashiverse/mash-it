package com.mashiverse.mashit.ui.dialogs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mashiverse.mashit.data.models.dialog.DialogContent
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ExtraSmallPadding
import com.mashiverse.mashit.ui.theme.Secondary

@Composable
fun Dialog(
    dialogContent: DialogContent,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = dialogContent.icon,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(ExtraSmallPadding))

                Text(text = dialogContent.title)
            }
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
                Text("Close", color = ContentColor)
            }
        },
        confirmButton = { },
        containerColor = Secondary,
        titleContentColor = ContentAccentColor,
        textContentColor = ContentColor
    )
}