package dev.tymoshenko.mashit.ui.screens.main.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.tymoshenko.mashit.ui.theme.SearchBarBackground

@Composable
fun Shop() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(SearchBarBackground))
    Text("Shop")
}