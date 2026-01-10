package io.mashit.mashit.ui.screens.main.artists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.mashit.mashit.ui.screens.main.header.CategoryHeader
import io.mashit.mashit.ui.theme.PaddingSize


@Composable
fun Artists() {
    Column(
        modifier = Modifier
    ) {
        CategoryHeader("Artists")
    }
}