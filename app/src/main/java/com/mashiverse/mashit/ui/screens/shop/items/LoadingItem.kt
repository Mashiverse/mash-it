package com.mashiverse.mashit.ui.screens.shop.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mashiverse.mashit.ui.theme.ContainerColor
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ContentContainerHeight
import com.mashiverse.mashit.ui.theme.ContentContainerShape

@Composable
fun SectionLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(ContentContainerHeight)
            .clip(ContentContainerShape)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = ContentAccentColor
        )
    }
}