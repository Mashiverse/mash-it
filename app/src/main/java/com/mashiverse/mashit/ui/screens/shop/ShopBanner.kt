package com.mashiverse.mashit.ui.screens.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

import com.mashiverse.mashit.ui.theme.MashiHolderShape
import com.mashiverse.mashit.ui.theme.SearchBarBackground

@Composable
fun ShopBanner(i: Int) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp)
            .clip(MashiHolderShape)
            .background(SearchBarBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "${i}")
    }
}