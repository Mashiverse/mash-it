package com.mashiverse.mashit.ui.screens.mashup

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
import com.mashiverse.mashit.data.states.intents.ImageIntent
import com.mashiverse.mashit.data.states.intents.MashupIntent
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.ui.screens.components.nft.trait.MintedTrait
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ExtraSmallPaddingSize
import com.mashiverse.mashit.ui.theme.MashiHolderShape

@Composable
fun MashupTraitHolder(
    width: Dp,
    height: Dp,
    mashupTrait: MashupTrait,
    processMashupIntent: (MashupIntent) -> Unit,
    processImageIntent: (ImageIntent) -> Unit
) {
    var avatarName = mashupTrait.avatarName.substringBefore("#").trimIndent()
    avatarName = if (avatarName.length > 16) {
        "${avatarName.take(13)}..."
    } else {
        avatarName
    }

    Column(
        modifier = Modifier
            .width(width)
    ) {
        MintedTrait(
            modifier = Modifier
                .width(width)
                .height(height)
                .border(width = 0.2.dp, shape = MashiHolderShape, color = ContentColor),
            onClick = { processMashupIntent(MashupIntent.OnMashupUpdate(mashupTrait)) },
            data = mashupTrait.trait.url ?: "",
            processImageIntent = processImageIntent,
            mint = mashupTrait.mint
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(
            text = avatarName,
            color = ContentAccentColor,
            fontSize = 12.sp
        )
    }
}