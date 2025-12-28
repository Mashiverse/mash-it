package dev.tymoshenko.mashit.ui.screens.main.mashup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.data.models.mashi.MashupTrait
import dev.tymoshenko.mashit.data.models.mashi.Trait
import dev.tymoshenko.mashit.ui.screens.main.mashi.TraitHolder
import dev.tymoshenko.mashit.ui.theme.ContentAccentColor
import dev.tymoshenko.mashit.ui.theme.ExtraSmallPaddingSize

@Composable
fun MashupTraitHolder(
    mashiHolderWidth: Dp,
    mashiHolderHeight: Dp,
    trait: MashupTrait,
    changeMashupTrait: (Trait) -> Unit,
) {
    var avatarName = trait.avatarName.substringBefore("#").trimIndent()
    avatarName = if (avatarName.length > 16) {
        avatarName.take(13) + "..."
    } else {
        avatarName
    }

    Column(
        modifier = Modifier
            .width(mashiHolderWidth)
    ) {
        TraitHolder(
            width = mashiHolderWidth,
            height = mashiHolderHeight,
            onClick = { changeMashupTrait.invoke(trait.trait) },
            data = trait.trait.url,
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(
            text = avatarName,
            color = ContentAccentColor,
            fontSize = 12.sp
        )
    }
}