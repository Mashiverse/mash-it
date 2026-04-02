package com.mashiverse.mashit.ui.screens.components.nav.drawer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.ui.theme.AccountInfoShape
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.Surface

@Composable
fun BaseButton(
    onConnect: () -> Unit,
    wallet: String?
) {
    OutlinedButton(
        modifier = Modifier

            .fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors().copy(
            containerColor = Color.Transparent,
            contentColor = Color.Red,
        ),
        border = BorderStroke(width = 1.dp, color = Surface),
        shape = AccountInfoShape,
        contentPadding = PaddingValues(horizontal = Padding),
        onClick = onConnect
    ) {
        Text(
            text = if (wallet != null) {
                "${wallet.take(6).lowercase()}...${wallet.substring(wallet.length - 4).lowercase()}"
            } else {
                "Connect Base"
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}