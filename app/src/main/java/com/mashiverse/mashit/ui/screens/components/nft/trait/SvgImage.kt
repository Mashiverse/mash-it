package com.mashiverse.mashit.ui.screens.components.nft.trait

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.ui.theme.MashiHolderShape
import com.mashiverse.mashit.utils.decoders.SvgCustomDecoder

@Composable
fun SvgImage(
    modifier: Modifier,
    data: String,
    selectedColors: SelectedColors?,
    contentScale: ContentScale
) {
    val ctx = LocalContext.current

    var cachedPainter by remember { mutableStateOf<Painter?>(null) }

    val svgLoader = remember(ctx, selectedColors) {
        ImageLoader.Builder(ctx)
            .components {
                add(SvgCustomDecoder.Factory(selectedColors = selectedColors))
                add(SvgDecoder.Factory())
            }
            .build()
    }

    val request = remember(data) {
        ImageRequest.Builder(ctx)
            .data(data)
            .crossfade(false)
            .build()
    }

    Box(
        modifier = modifier
            .clip(MashiHolderShape)
    ) {
        cachedPainter?.let {
            Image(
                painter = it,
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = contentScale,
            )
        }

        AsyncImage(
            model = request,
            imageLoader = svgLoader,
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = contentScale,
            onState = { state ->
                if (state is AsyncImagePainter.State.Success) {
                    cachedPainter = state.painter
                }
            }
        )
    }
}