package com.mashiverse.mashit.ui.screens.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.SmallPadding

@Composable
fun CheckRow(title: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${title}:",
            fontSize = 14.sp,
            color = ContentAccentColor,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(SmallPadding))

        Switch(
            checked = checked,
            onCheckedChange = { checked -> onChange.invoke(checked) }
        )
    }
}