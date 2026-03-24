package com.mashiverse.mashit.ui.screens.mashup.actions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.mashiverse.mashit.data.states.intents.ImageIntent
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.nft.TraitType
import com.mashiverse.mashit.ui.screens.components.nft.trait.MintText
import com.mashiverse.mashit.ui.screens.components.nft.trait.TraitImage
import com.mashiverse.mashit.ui.theme.Tertiary
import com.mashiverse.mashit.ui.theme.MashiHolderShape
import com.mashiverse.mashit.ui.theme.MaxMashiHolderWidth

@Composable
fun MashupComposite(
    mashupDetails: MashupDetails,
    modifier: Modifier = Modifier,
    processImageIntent: (ImageIntent) -> Unit,
    holderWidth: Dp
) {
    Box(
        modifier = modifier
            .clip(MashiHolderShape)
            .background(Tertiary),
        contentAlignment = Alignment.Center
    ) {
        val maxWidth = min(holderWidth, MaxMashiHolderWidth)
        mashupDetails.assets.sortedBy { it.type }.forEach { trait ->
            val traitType = trait.type
            val width =
                if (traitType == TraitType.BACKGROUND) maxWidth else maxWidth * 380 / 552
            val height =
                if (traitType == TraitType.BACKGROUND) maxWidth * 4 / 3 else (maxWidth * 4 / 3) * 600 / 736
            val contentScale =
                if (traitType == TraitType.BACKGROUND) ContentScale.FillBounds else ContentScale.Fit

            TraitImage(
                modifier = Modifier
                    .width(width)
                    .height(height),
                background = Color.Transparent,
                selectedColors = mashupDetails.colors,
                data = trait.url ?: "",
                contentScale = contentScale,
                processImageIntent = processImageIntent
            )
        }

        mashupDetails.mint?.let {
            MintText(
                modifier = Modifier
                    .padding(end = 2.dp)
                    .align(Alignment.BottomEnd),
                mint = mashupDetails.mint
            )
        }
    }
}