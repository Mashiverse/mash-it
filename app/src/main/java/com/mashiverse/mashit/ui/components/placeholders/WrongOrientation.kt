package com.mashiverse.mashit.ui.components.placeholders

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.R
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.SmallPaddingSize

@Composable
fun WrongOrientation() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(32.dp),
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.width(SmallPaddingSize))

            Text(
                text = "Please rotate your phone",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ContentAccentColor
            )
        }
    }
}