package dev.tymoshenko.mashit.ui.screens.main.mashi.trait

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.data.models.mashi.MashiTrait
import dev.tymoshenko.mashit.ui.theme.ContentAccentColor
import dev.tymoshenko.mashit.ui.theme.ExtraSmallPaddingSize
import dev.tymoshenko.mashit.ui.theme.MashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.MashiHolderWidth

@Composable
fun TraitHolder(
    mashiTrait: MashiTrait,
    width: Dp = MashiHolderWidth,
    height: Dp = MashiHolderHeight,
) {
    Column {
        Trait(
            modifier = Modifier
                .width(width)
                .height(height),
            data = mashiTrait.url,
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(
            fontSize = 14.sp,
            text = mashiTrait.traitType.name
                .lowercase()
                .replace("_", " ")
                .replaceFirstChar { c -> c.uppercaseChar() },
            color = ContentAccentColor,
            fontWeight = FontWeight.Bold
        )
    }
}