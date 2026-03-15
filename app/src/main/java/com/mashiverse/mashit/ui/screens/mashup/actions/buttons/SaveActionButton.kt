package com.mashiverse.mashit.ui.screens.mashup.actions.buttons

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.ui.theme.Primary
import com.mashiverse.mashit.ui.theme.ContentAccentColor

@Composable
fun SaveActionButton(
    onSave: () -> Unit
) {
    Button(
        modifier = Modifier
            .width(56.dp)
            .height(32.dp),
        shape = RoundedCornerShape(90),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = Primary,
            contentColor = ContentAccentColor
        ),
        onClick = onSave
    ) {
        Text(
            modifier = Modifier
                .wrapContentSize(unbounded = true),
            text = "Save"
        )
    }
}