package dev.tymoshenko.mashit.ui.screens.main.mashi.trait

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.gif.AnimatedImageDecoder
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import dev.tymoshenko.mashit.data.models.color.SelectedColors
import dev.tymoshenko.mashit.ui.theme.MashiBackground
import dev.tymoshenko.mashit.ui.theme.MashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.MashiHolderShape
import dev.tymoshenko.mashit.ui.theme.MashiHolderWidth
import dev.tymoshenko.mashit.utils.decoders.SvgCustomDecoderFactory

val maskingMatrix = ColorMatrix(
    floatArrayOf(
        1f, 0f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f, 0f,
        0f, 0f, 1f, 0f, 0f,
        0f, 0f, 0f, 100f, -10f
    )
)

val commonMatrix = ColorMatrix(
    floatArrayOf(
        1f, 0f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f, 0f,
        0f, 0f, 1f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    )
)

@Composable
fun Trait(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    width: Dp = MashiHolderWidth,
    height: Dp = MashiHolderHeight,
    data: String = "https://example.com/image.svg",
    background: Color = MashiBackground,
    selectedColors: SelectedColors? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    val ctx = LocalContext.current
    var hasMask by remember(data) { mutableStateOf(false) }
    val onMaskDetection = { isMaskDetected: Boolean ->
        hasMask = isMaskDetected
    }

    var lastPainter by remember { mutableStateOf<Painter?>(null) }

    // Always use a default loader for non-SVGs
    val defaultLoader = remember {
        ImageLoader.Builder(ctx)
            .components {
                add(AnimatedImageDecoder.Factory())
                add(SvgDecoder.Factory())
            }
            .build()
    }

    // State for SVG detection
    var isSvg by remember(data) { mutableStateOf<Boolean?>(true) }
    val onSvgDetection = { isDetected: Boolean ->
        isSvg = isDetected
    }

    // Only create a special loader for SVGs, otherwise use defaultLoader
    val imageLoader = if (isSvg == true) {
        remember(selectedColors) {
            ImageLoader.Builder(ctx)
                .components {
                    add(
                        SvgCustomDecoderFactory(
                            selectedColors = selectedColors,
                            onMaskDetection = onMaskDetection,
                            onSvgDetection = onSvgDetection
                        )
                    )
                    add(AnimatedImageDecoder.Factory())
                    add(SvgDecoder.Factory())
                }
                .build()
        }
    } else {
        defaultLoader
    }

    val request = ImageRequest.Builder(ctx)
        .data(data)
        .crossfade(false)
        .build()

    Box( modifier = modifier
        .width(width)
        .height(height)
        .clip(MashiHolderShape)
    ) {
        lastPainter?.let {
            Image(
                modifier = modifier
                    .width(width)
                    .height(height)
                    .clip(MashiHolderShape)
                    .background(background)
                    .clickable(onClick = onClick),
                painter = it,
                contentDescription = null,
                contentScale = contentScale,
                colorFilter = ColorFilter.colorMatrix(if (hasMask) maskingMatrix else commonMatrix)
            )
        }

        AsyncImage(
            modifier = modifier
                .width(width)
                .height(height)
                .clip(MashiHolderShape)
                .background(background)
                .clickable(onClick = onClick),
            alignment = Alignment.Center,
            colorFilter = ColorFilter.colorMatrix(if (hasMask) maskingMatrix else commonMatrix),
            imageLoader = imageLoader,
            model = request,
            contentDescription = "Mashi",
            contentScale = contentScale,
            onState = { state: AsyncImagePainter.State ->
                lastPainter = if (state is AsyncImagePainter.State.Success) {
                    state.painter
                } else {
                    null
                }
            }
        )
    }

}


@Preview
@Composable
private fun TraitPreview() {
    Trait()
}
