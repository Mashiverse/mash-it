package com.mashiverse.mashit.ui.screens.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.ui.theme.ContentContainerShape
import com.mashiverse.mashit.ui.theme.Primary
import com.mashiverse.mashit.ui.theme.Secondary

@Composable
fun BaseButton(
    onConnect: () -> Unit,
    wallet: String?
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = Secondary,
            contentColor = Primary,
        ),
        shape = ContentContainerShape,
        onClick = onConnect
    ) {
        Text(
            text = if (wallet != null) {
                "Disconnect"
            } else {
                "Connect Base"
            },
            fontSize = 16.sp
        )
    }
}