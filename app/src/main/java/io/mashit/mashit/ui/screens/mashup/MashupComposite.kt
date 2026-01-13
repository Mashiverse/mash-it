package io.mashit.mashit.ui.screens.mashup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import io.mashit.mashit.data.models.mashi.MashupDetails
import io.mashit.mashit.data.models.mashi.MashupTrait
import io.mashit.mashit.data.models.mashi.TraitType
import io.mashit.mashit.ui.screens.mashi.trait.Trait
import io.mashit.mashit.ui.theme.ExtraLargeMashiHolderHeight
import io.mashit.mashit.ui.theme.ExtraLargeMashiHolderWidth
import io.mashit.mashit.ui.theme.ExtraLargeTraitHolderHeight
import io.mashit.mashit.ui.theme.ExtraLargeTraitHolderWidth
import io.mashit.mashit.ui.theme.MashiBackground
import io.mashit.mashit.ui.theme.MashiHolderShape
import timber.log.Timber

@Composable
fun MashupComposite(mashupDetails: MashupDetails) {
    Box(
        modifier = Modifier
            .height(ExtraLargeMashiHolderHeight)
            .width(ExtraLargeMashiHolderWidth)
            .clip(MashiHolderShape)
            .background(MashiBackground),
        contentAlignment = Alignment.Center
    ) {
        mashupDetails.assets?.sortedBy { it.traitType }?.forEach { trait ->
            val traitType = trait.traitType
            val width =
                if (traitType == TraitType.BACKGROUND) ExtraLargeMashiHolderWidth else ExtraLargeTraitHolderWidth
            val height =
                if (traitType == TraitType.BACKGROUND) ExtraLargeMashiHolderHeight else ExtraLargeTraitHolderHeight
            val contentScale =
                if (traitType == TraitType.BACKGROUND) ContentScale.FillBounds else ContentScale.Fit

            Timber.tag("GG").d(trait.url)

            Trait(
                modifier = Modifier
                    .width(width)
                    .height(height),
                background = Color.Transparent,
                selectedColors = mashupDetails.colors,
                data = trait.url,
                contentScale = contentScale
            )
        }
    }
}