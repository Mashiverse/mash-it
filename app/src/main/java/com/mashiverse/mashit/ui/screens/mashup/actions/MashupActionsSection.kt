package com.mashiverse.mashit.ui.screens.mashup.actions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mashiverse.mashit.ui.theme.LargeMashiHolderHeight

@Composable
fun MashupActionsSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(LargeMashiHolderHeight)
    ) {
        Column(
            modifier =
                Modifier
                    .align(Alignment.CenterStart)
        ) { }
    }
}