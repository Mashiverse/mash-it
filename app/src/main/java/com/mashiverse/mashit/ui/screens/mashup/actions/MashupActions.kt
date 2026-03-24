package com.mashiverse.mashit.ui.screens.mashup.actions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.states.intents.ImageIntent
import com.mashiverse.mashit.data.states.intents.ActionsIntent
import com.mashiverse.mashit.data.models.mashup.MashupDetails

@Composable
fun MashupActions(
    mashupDetails: MashupDetails,
    modifier: Modifier = Modifier,
    holderWidth: Dp,
    processImageIntent: (ImageIntent) -> Unit,
    processActionsIntent: (ActionsIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .widthIn(0.dp, 400.dp)
                .align(Alignment.Center)
        ){
            LeftPanel(processActionsIntent = processActionsIntent)

            Spacer(modifier = Modifier.weight(1F))

            MashupComposite(
                modifier = modifier,
                mashupDetails = mashupDetails,
                holderWidth = holderWidth,
                processImageIntent = processImageIntent,
            )

            Spacer(modifier = Modifier.weight(1F))

            RightPanel(processActionsIntent = processActionsIntent)
        }
    }
}