package dev.tymoshenko.mashit.ui.screens.main.artists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.tymoshenko.mashit.ui.screens.main.header.CategoryHeader
import dev.tymoshenko.mashit.ui.theme.PaddingSize


@Composable
fun Artists() {
    Column(
        modifier = Modifier
            .padding(PaddingSize)
    ) {
        CategoryHeader("Artists")
    }
}