package com.mashiverse.mashit.ui.default.indicators

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentContainerHeight
import com.mashiverse.mashit.ui.theme.ContentContainerShape
import com.mashiverse.mashit.ui.theme.Secondary

@Composable
fun SectionLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(ContentContainerHeight)
            .clip(ContentContainerShape)
            .background(Secondary)

    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center),
            color = ContentAccentColor
        )
    }
}

@Composable
@Preview
private fun SectionLoadingPreview() {
    SectionLoading()
}