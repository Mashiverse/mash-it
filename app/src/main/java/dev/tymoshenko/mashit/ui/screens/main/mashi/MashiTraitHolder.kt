package dev.tymoshenko.mashit.ui.screens.main.mashi

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.ui.theme.MashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.MashiHolderWidth

@Composable
fun MashiTraitHolder(
    width: Dp = MashiHolderWidth,
    height: Dp = MashiHolderHeight
) {
    Column {
        MashiHolder(
            width = width,
            height = height
        )

        Text(fontSize = 14.sp, text = "Trait", color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
@Preview
private fun MashiTraitHolderPreview() {
    MashiTraitHolder()
}