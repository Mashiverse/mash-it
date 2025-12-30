package dev.tymoshenko.mashit.ui.screens.main.mashi.trait

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
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
import dev.tymoshenko.mashit.utils.decoders.SvgCustomDecoderFactory

val maskingMatrix = ColorMatrix(
    floatArrayOf(
        1f, 0f, 0f, 0f, 0f,       // Red (Keep as is)
        0f, 1f, 0f, 0f, 0f,       // Green (Keep as is)
        0f, 0f, 1f, 0f, 0f,       // Blue (Keep as is)
        0f, 0f, 0f, 100f, -10f     // Alpha: Multiply by 100, subtract 1
    )
)

val commonMatrix = ColorMatrix(
    floatArrayOf(
        1f, 0f, 0f, 0f, 0f, // Red:   100% of original Red
        0f, 1f, 0f, 0f, 0f, // Green: 100% of original Green
        0f, 0f, 1f, 0f, 0f, // Blue:  100% of original Blue
        0f, 0f, 0f, 1f, 0f  // Alpha: 100% of original Alpha
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
    selectedColors: SelectedColors? = null
) {
    val ctx = LocalContext.current
    var hasMask by remember(data) { mutableStateOf(false) }
    val onMaskDetection = { isMaskDetected: Boolean ->
        hasMask = isMaskDetected
    }

    val imageLoader = remember(selectedColors) {
        ImageLoader.Builder(ctx)
            .components {
                add(
                    SvgCustomDecoderFactory(
                        selectedColors = selectedColors,
                        onMaskDetection = onMaskDetection
                    )
                )
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
//            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .clickable(onClick = onClick),
        alignment = Alignment.Center,
        colorFilter = ColorFilter.colorMatrix(if (hasMask) maskingMatrix else commonMatrix),
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