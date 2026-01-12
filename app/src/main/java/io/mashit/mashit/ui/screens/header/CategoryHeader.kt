package io.mashit.mashit.ui.screens.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import io.mashit.mashit.ui.theme.ContentAccentColor
import io.mashit.mashit.ui.theme.ExtraSmallPaddingSize

@Composable
fun CategoryHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(text = title, fontWeight = FontWeight.Bold, color = ContentAccentColor)

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))
    }
}