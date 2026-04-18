package com.mashiverse.mashit.ui.default.buttons


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.ui.theme.ContentContainerHeight
import com.mashiverse.mashit.ui.theme.ContentContainerShape
import com.mashiverse.mashit.utils.helpers.openSocialLink

@Composable
fun RedirectButton(
    uri: String,
    background: Color,
    text: String
) {
    val context = LocalContext.current

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(ContentContainerHeight),
        onClick = { openSocialLink(context, uri) },
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = background,
            contentColor = Color.White
        ),
        shape = ContentContainerShape
    ) {
        Text(
            text = text,
            fontSize = 16.sp
        )
    }
}