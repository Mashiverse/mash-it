package dev.tymoshenko.mashit.ui.screens.main.mashi.trait

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.gif.AnimatedImageDecoder
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import dev.tymoshenko.mashit.data.models.color.SelectedColors
import dev.tymoshenko.mashit.ui.theme.MashiBackground
import dev.tymoshenko.mashit.ui.theme.MashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.MashiHolderShape
import dev.tymoshenko.mashit.ui.theme.MashiHolderWidth
import dev.tymoshenko.mashit.utils.decoders.SvgColorReplacementDecoderFactory


@Composable
fun Trait(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    width: Dp = MashiHolderWidth,
    height: Dp = MashiHolderHeight,
    data: String = "https://example.com/image.svg",
    background: Color = MashiBackground,
    selectedColors: SelectedColors? = null
) {
    val ctx = LocalContext.current

    val imageLoader = remember(selectedColors) {
        ImageLoader.Builder(ctx)
            .components {
                if (selectedColors != null) {
                    add(
                        SvgColorReplacementDecoderFactory(
                            hair = selectedColors.hair,
                            eyes = selectedColors.eyes,
                            body= selectedColors.body
                        )
                    )
                }
                add(AnimatedImageDecoder.Factory())
                add(SvgDecoder.Factory())
            }
            .build()
    }

    val request = ImageRequest.Builder(ctx)
        .data(data)
        .crossfade(false)
        .build()

    AsyncImage(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(MashiHolderShape)
            .background(background)
            .clickable(onClick = onClick),
        alignment = Alignment.Center,
        imageLoader = imageLoader,
        model = request,
        contentDescription = "Mashi",
        contentScale = ContentScale.Fit
    )
}


@Preview
@Composable
private fun TraitPreview() {
    Trait()
}