package com.mashiverse.mashit.ui.screens.shop.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.ui.theme.ContainerColor
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentContainerHeight
import com.mashiverse.mashit.ui.theme.ContentContainerShape
import com.mashiverse.mashit.ui.theme.IconSize

@Composable
fun SectionRefresh(
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(ContentContainerHeight)
            .clip(ContentContainerShape)
            .background(ContainerColor)
    ) {
        IconButton(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center),
            onClick = onRetry
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Retry",
                tint = ContentAccentColor
            )
        }
    }
}

@Preview
@Composable
fun SectionRefreshPreview() {
    SectionRefresh(
        onRetry = {}
    )
}