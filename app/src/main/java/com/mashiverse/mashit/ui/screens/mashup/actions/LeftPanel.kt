package com.mashiverse.mashit.ui.screens.mashup.actions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.times
import com.mashiverse.mashit.data.intents.ActionsIntent
import com.mashiverse.mashit.ui.screens.mashup.actions.buttons.ActionButton
import com.mashiverse.mashit.ui.screens.mashup.actions.buttons.ColorSelectActionButton
import com.mashiverse.mashit.ui.theme.SmallPaddingSize

@Composable
fun LeftPanel(processActionsIntent: (ActionsIntent) -> Unit) {
    Column {
        Spacer(modifier = Modifier.height(SmallPaddingSize))

        ColorSelectActionButton(onColor = { processActionsIntent(ActionsIntent.OnColor) })

        Spacer(modifier = Modifier.height(3 * SmallPaddingSize))

        ActionButton(
            icon = Icons.Default.Refresh,
            onClick = { processActionsIntent(ActionsIntent.OnRandom) }
        )

        Spacer(modifier = Modifier.height(SmallPaddingSize))

        ActionButton(
            icon = Icons.Default.Slideshow,
            onClick = { processActionsIntent(ActionsIntent.OnPreview) },
        )

        Spacer(modifier = Modifier.height(SmallPaddingSize))

        ActionButton(
            icon = Icons.Default.Delete,
            onClick = { processActionsIntent(ActionsIntent.OnReset) },
        )
    }
}