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
import dev.tymoshenko.mashit.ui.theme.TextColor

@Composable
fun OtherSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            onClick = {},
            shape = RoundedCornerShape(25),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = Color.Transparent,
                contentColor = TextColor
            )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .offset(x = (-6).dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Mash-It Rules", fontSize = 16.sp, fontWeight = FontWeight.Normal)
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            onClick = {},
            shape = RoundedCornerShape(25),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = Color.Transparent,
                contentColor = Color.Red
            )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .offset(x = (-6).dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Help Center", fontSize = 16.sp, fontWeight = FontWeight.Normal)
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            onClick = {},
            shape = RoundedCornerShape(25),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = Color.Transparent,
                contentColor = TextColor
            )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .offset(x = (-6).dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Sign Out", fontSize = 16.sp, fontWeight = FontWeight.Normal)
            }
        }
    }
}