package com.mashiverse.mashit.ui.components.nfts.trait

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.ui.theme.MashiBackground

@Composable
fun MintedTrait(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    data: String,
    getImageType: (String) -> ImageType?,
    setImageType: (ImageType, String) -> Unit,
    background: Color = MashiBackground,
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
            getImageType = getImageType,
            setImageType = setImageType,
            background = background,
            selectedColors = selectedColors,
            contentScale = contentScale
        )

        mint?.let {
            MintText(
                modifier = Modifier
                    .padding(end = 2.dp)
                    .align(Alignment.BottomEnd),
                mint = mint
            )
        }
    }
}