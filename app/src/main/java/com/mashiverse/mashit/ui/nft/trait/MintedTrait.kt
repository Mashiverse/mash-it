package com.mashiverse.mashit.ui.nft.trait

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.data.intents.ImageIntent

@Composable
fun MintedTrait(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    data: String,
    processImageIntent: (ImageIntent) -> Unit,
    selectedColors: SelectedColors? = null,
    contentScale: ContentScale = ContentScale.Fit,
    mint: Int? = null
) {
    Box(
        modifier = modifier
    ) {
        TraitImage(
            modifier = Modifier.fillMaxSize(),
            onClick = onClick,
            data = data,
            processImageIntent = processImageIntent,
            selectedColors = selectedColors,
            contentScale = contentScale
        )

        mint?.let {
            MintText(
                modifier = Modifier
                    .padding(bottom = 2.dp, end = 3.dp)
                    .align(Alignment.BottomEnd),
                mint = mint
            )
        }
    }
}