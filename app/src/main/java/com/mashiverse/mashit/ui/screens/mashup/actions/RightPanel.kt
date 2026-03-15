package com.mashiverse.mashit.ui.screens.mashup.actions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.times
import com.mashiverse.mashit.data.intents.ActionsIntent
import com.mashiverse.mashit.data.models.image.DownloadImageType
import com.mashiverse.mashit.ui.screens.mashup.actions.buttons.ActionButton
import com.mashiverse.mashit.ui.screens.mashup.actions.buttons.SaveActionButton
import com.mashiverse.mashit.ui.theme.SmallPaddingSize

@Composable
fun RightPanel(processMashupIntent: (ActionsIntent) -> Unit) {
    val context = LocalContext.current

    Column {
        Spacer(modifier = Modifier.height(SmallPaddingSize))

        SaveActionButton(onSave = { processMashupIntent(ActionsIntent.OnSave) })

        Spacer(modifier = Modifier.height(3 * SmallPaddingSize))

        ActionButton(
            icon = Icons.Default.Download,
            onClick = {
                processMashupIntent(
                    ActionsIntent.OnImageSave(
                        context = context,
                        downloadType = DownloadImageType.PNG
                    )
                )
            }
        )

        Spacer(modifier = Modifier.height(SmallPaddingSize))

        ActionButton(
            icon = Icons.Default.Download,
            onClick = {
                processMashupIntent(
                    ActionsIntent.OnImageSave(
                        context = context,
                        downloadType = DownloadImageType.GIF
                    )
                )
            },
            isAnimated = true
        )

        Spacer(modifier = Modifier.height(SmallPaddingSize))

        ActionButton(
            icon = Icons.Default.Redo,
            onClick = { processMashupIntent(ActionsIntent.OnRedo) }
        )

        Spacer(modifier = Modifier.height(SmallPaddingSize))

        ActionButton(
            icon = Icons.Default.Undo,
            onClick = { processMashupIntent(ActionsIntent.OnUndo) },
        )
    }
}