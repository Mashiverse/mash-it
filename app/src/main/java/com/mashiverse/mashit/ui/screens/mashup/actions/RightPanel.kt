package com.mashiverse.mashit.ui.screens.mashup.actions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.times
import com.mashiverse.mashit.data.models.image.DownloadType
import com.mashiverse.mashit.data.states.intents.ActionsIntent
import com.mashiverse.mashit.ui.screens.mashup.actions.buttons.ActionButton
import com.mashiverse.mashit.ui.screens.mashup.actions.buttons.SaveActionButton
import com.mashiverse.mashit.ui.theme.SmallPadding

@Composable
fun RightPanel(processActionsIntent: (ActionsIntent) -> Unit) {
    val context = LocalContext.current

    Column {
        Spacer(modifier = Modifier.height(SmallPadding))

        SaveActionButton(onSave = { processActionsIntent(ActionsIntent.OnSave) })

        Spacer(modifier = Modifier.height(3 * SmallPadding))

        ActionButton(
            icon = Icons.Default.Download,
            onClick = {
                processActionsIntent(
                    ActionsIntent.OnImageSave(
                        context = context,
                        downloadType = DownloadType.PNG
                    )
                )
            }
        )

        Spacer(modifier = Modifier.height(SmallPadding))

        ActionButton(
            icon = Icons.Default.Download,
            onClick = {
                processActionsIntent(
                    ActionsIntent.OnImageSave(
                        context = context,
                        downloadType = DownloadType.GIF
                    )
                )
            },
            isAnimated = true
        )

        Spacer(modifier = Modifier.height(SmallPadding))

        ActionButton(
            icon = Icons.AutoMirrored.Filled.Redo,
            onClick = { processActionsIntent(ActionsIntent.OnRedo) }
        )

        Spacer(modifier = Modifier.height(SmallPadding))

        ActionButton(
            icon = Icons.AutoMirrored.Filled.Undo,
            onClick = { processActionsIntent(ActionsIntent.OnUndo) },
        )
    }
}