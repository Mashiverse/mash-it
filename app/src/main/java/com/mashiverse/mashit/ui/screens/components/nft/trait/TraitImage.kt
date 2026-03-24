package com.mashiverse.mashit.ui.screens.components.nft.trait

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.states.intents.ImageIntent
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.data.states.utils.rememberImageType
import com.mashiverse.mashit.ui.theme.MashiHolderShape
import com.mashiverse.mashit.ui.theme.Tertiary
import com.mashiverse.mashit.utils.MASHIVERSE_BASE_URL


@Composable
fun TraitImage(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    data: String,
    processImageIntent: (ImageIntent) -> Unit,
    background: Color = Tertiary,
    selectedColors: SelectedColors? = null,
    contentScale: ContentScale = ContentScale.Fit
) {

    val imageType by rememberImageType(
        data = data,
        processImageIntent = processImageIntent
    )

    Box(
        modifier = modifier
            .clip(MashiHolderShape)
            .background(background)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else Modifier
            )
    ) {
        if (imageType != null) {
            when (imageType) {
                ImageType.SVG, ImageType.SVG_MASK -> {
                    val newData = when (imageType) {
                        ImageType.SVG -> data
                        ImageType.SVG_MASK -> "${MASHIVERSE_BASE_URL}api/svg/${
                            data.split("/").last()
                        }"

                        else -> ""
                    }
                    SvgImage(
                        modifier = modifier,
                        data = newData,
                        selectedColors = selectedColors,
                        contentScale = contentScale
                    )
                }

                ImageType.APNG -> {
                    val newData = "${MASHIVERSE_BASE_URL}api/apng/${data.split("/").last()}"
                    NonSvgImage(
                        modifier = modifier,
                        data = newData,
                        contentScale = contentScale
                    )
                }

                else -> {
                    NonSvgImage(
                        modifier = modifier,
                        data = data,
                        contentScale = contentScale
                    )
                }
            }
        }
    }
}