package io.mashit.mashit.ui.screens.mashi.trait

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.mashit.mashit.data.models.mashi.MashupTrait
import io.mashit.mashit.data.models.mashi.MashiTrait
import io.mashit.mashit.ui.theme.ContentAccentColor
import io.mashit.mashit.ui.theme.ContentColor
import io.mashit.mashit.ui.theme.ExtraSmallPaddingSize
import io.mashit.mashit.ui.theme.MashiHolderShape

@Composable
fun MashupTraitHolder(
    mashiHolderWidth: Dp,
    mashiHolderHeight: Dp,
    trait: MashupTrait,
    changeMashupTrait: (MashiTrait) -> Unit,
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
        Trait(
            modifier = Modifier
                .width(mashiHolderWidth)
                .height(mashiHolderHeight)
                .border(width = 0.2.dp, shape = MashiHolderShape, color = ContentColor),
            onClick = { changeMashupTrait.invoke(trait.mashiTrait) },
            data = trait.mashiTrait.url,
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(
            text = avatarName,
            color = ContentAccentColor,
            fontSize = 12.sp
        )
    }
}