package com.mashiverse.mashit.ui.components.placeholders

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.R

@Composable
fun NotConnected() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "You need to connect your wallet to see \n" +
                        "this page!",
                fontSize = 14.sp,
                color = Color.Red,
                textAlign = TextAlign.Center
            )

            Image(
                painter = painterResource(R.drawable.logo),
                modifier = Modifier.size(128.dp)
                    .offset(y = (-16).dp),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}