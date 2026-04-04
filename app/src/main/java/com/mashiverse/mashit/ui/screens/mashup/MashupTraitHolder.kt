package com.mashiverse.mashit.ui.screens.mashup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.states.intents.ImageIntent
import com.mashiverse.mashit.data.states.intents.MashupIntent
import com.mashiverse.mashit.ui.screens.components.nft.trait.TraitImage
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ExtraSmallPadding
import com.mashiverse.mashit.ui.theme.Primary
import com.mashiverse.mashit.ui.theme.Surface
import com.mashiverse.mashit.ui.theme.TraitShape

@Composable
fun MashupTraitHolder(
    width: Dp,
    height: Dp,
    isSelected: Boolean = false,
    mashupTrait: MashupTrait,
    processMashupIntent: (MashupIntent) -> Unit,
    processImageIntent: (ImageIntent) -> Unit
) {
    var avatarName = mashupTrait.avatarName.substringBefore("#").trimIndent()

    Column {
        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .clip(TraitShape)
                .background(Surface)
                .border(
                    width = 1.dp,
                    shape = TraitShape,
                    color = if (isSelected) Primary else ContentColor
                )
                .padding(4.dp),
        ) {
            TraitImage(
                modifier = Modifier,
                onClick = { processMashupIntent(MashupIntent.OnMashupUpdate(mashupTrait)) },
                data = mashupTrait.trait.url ?: "",
                processImageIntent = processImageIntent
            )
        }

        Spacer(modifier = Modifier.height(ExtraSmallPadding))

        Row(
            modifier = Modifier.width(width),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = avatarName,
                color = ContentAccentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}