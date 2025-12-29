package dev.tymoshenko.mashit.ui.screens.main.mashi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.data.models.mashi.Trait
import dev.tymoshenko.mashit.ui.theme.ContentAccentColor
import dev.tymoshenko.mashit.ui.theme.ExtraSmallPaddingSize
import dev.tymoshenko.mashit.ui.theme.MashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.MashiHolderWidth

@Composable
fun MashiTraitHolder(
    trait: Trait,
    width: Dp = MashiHolderWidth,
    height: Dp = MashiHolderHeight,
) {
    Column {
        TraitHolder(
            width = width,
            height = height,
            data = trait.url,
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(
            fontSize = 14.sp,
            text = trait.traitType.name
                .lowercase()
                .replace("_", " ")
                .replaceFirstChar { c -> c.uppercaseChar() },
            color = ContentAccentColor,
            fontWeight = FontWeight.Bold
        )
    }
}