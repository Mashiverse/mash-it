package com.mashiverse.mashit.ui.screens.artists

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.artists.ArtistMashup
import com.mashiverse.mashit.data.states.intents.ImageIntent
import com.mashiverse.mashit.ui.screens.components.nft.trait.TraitImage
import com.mashiverse.mashit.ui.theme.ContentAccentColor

@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    artistMashup: ArtistMashup,
    processImageIntent: (ImageIntent) -> Unit,
) {
    Box(
        modifier = modifier
            .size(80.dp)
            .border(
                width = 2.dp,
                color = ContentAccentColor,
                shape = CircleShape
            )
            .clip(CircleShape)
    ) {
        artistMashup.layers.forEach { url ->
            TraitImage(
                modifier = Modifier.matchParentSize(),
                onClick = onClick,
                data = url,
                selectedColors = artistMashup.colors,
                processImageIntent = processImageIntent,
                contentScale = ContentScale.Crop
            )
        }
    }
}
