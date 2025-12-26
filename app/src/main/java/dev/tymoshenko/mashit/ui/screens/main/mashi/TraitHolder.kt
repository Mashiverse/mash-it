package dev.tymoshenko.mashit.ui.screens.main.mashi

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import dev.tymoshenko.mashit.data.models.mashi.MashiDetails
import dev.tymoshenko.mashit.ui.theme.MashiBackground
import dev.tymoshenko.mashit.ui.theme.MashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.MashiHolderShape
import dev.tymoshenko.mashit.ui.theme.MashiHolderWidth

@Composable
fun TraitHolder(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    width: Dp = MashiHolderWidth,
    height: Dp = MashiHolderHeight,
    data: String = "https://example.com/image.jpg",
) {
    val ctx = LocalContext.current

    val imageLoader = ImageLoader.Builder(ctx)
        .components {
            add(AnimatedImageDecoder.Factory())
            add(SvgDecoder.Factory())
        }
        .build()

    AsyncImage(
        modifier = modifier
            .width(width)
            .clickable(onClick = onClick)
            .height(height)
            .clip(MashiHolderShape)
            .background(MashiBackground),
        alignment = Alignment.Center,
        imageLoader = imageLoader,
        model = ImageRequest.Builder(ctx)
            .data(data)
            .crossfade(true)
            .build(),
        contentDescription = "Mashi",
        contentScale = ContentScale.Fit
    )
}

@Preview
@Composable
private fun TraitHolderPreview() {
    TraitHolder()
}