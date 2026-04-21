package com.mashiverse.mashit.ui.default.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.ui.theme.ContentContainerHeight
import com.mashiverse.mashit.ui.theme.ContentContainerShape
import com.mashiverse.mashit.ui.theme.Padding
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
            .padding(horizontal = Padding)
            .height(ContentContainerHeight),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = Secondary,
            contentColor = Primary,
        ),
        shape = ContentContainerShape,
        onClick = onConnect,
        contentPadding = PaddingValues(horizontal = Padding)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = if (wallet != null) {
                "Disconnect"
            } else {
                "Connect wallet"
            },
            textAlign = TextAlign.Start,
            fontSize = 16.sp
        )
    }
}