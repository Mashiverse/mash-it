package dev.tymoshenko.mashit.ui.screens.main.components.nav.drawer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.ui.theme.ContentColor

@Composable
private fun OtherSectionButton(
    onClick: () -> Unit,
    text: String,
    contentColor: Color = ContentColor,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        onClick = onClick,
        shape = RoundedCornerShape(25),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = Color.Transparent,
            contentColor = contentColor
        )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .offset(x = (-6).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Normal)
        }
    }
}

@Composable
fun OtherSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OtherSectionButton(
            onClick = {},
            text = "Mash-It Rules"
        )

        OtherSectionButton(
            onClick = {},
            text = "Help Center",
            contentColor = Color.Red
        )

        OtherSectionButton(
            onClick = {},
            text = "Sign Out",
        )
    }
}