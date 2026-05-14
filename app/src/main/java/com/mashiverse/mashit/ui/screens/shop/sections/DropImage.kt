package com.mashiverse.mashit.ui.screens.shop.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mashiverse.mashit.ui.theme.TraitShape

@Composable
fun DropImage(
    imageUrl: String,
    slug: String,
    onClick: (String) -> Unit
) {
    AsyncImage(
        modifier = Modifier
            .height((80 * 2).dp)
            .clip(TraitShape)
            .clickable {
                onClick.invoke(slug)
            },
        contentDescription = null,
        model = imageUrl,
        clipToBounds = true
    )
}