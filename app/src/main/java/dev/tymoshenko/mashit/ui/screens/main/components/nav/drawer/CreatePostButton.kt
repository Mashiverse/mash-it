package dev.tymoshenko.mashit.ui.screens.main.components.nav.drawer

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.ui.theme.ButtonContainerColor
import dev.tymoshenko.mashit.ui.theme.TextColor

@Composable
fun CreatePostButton(modifier: Modifier) {
    val ctx = LocalContext.current

    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp),
        onClick = {
            Toast.makeText(ctx, "Creates post", Toast.LENGTH_SHORT).show()
        },
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = ButtonContainerColor,
            contentColor = TextColor
        ),
        contentPadding = PaddingValues(horizontal = 4.dp),
        shape = RoundedCornerShape(25)
    ) {
        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp),
                imageVector = Icons.Default.Add,
                contentDescription = "add icon",
                tint = Color.Red
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(text = "Create a post", fontSize = 14.sp)
        }
    }
}

@Preview
@Composable
private fun CreatePostButtonPreview() {
    CreatePostButton(Modifier)
}