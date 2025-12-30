package dev.tymoshenko.mashit.ui.screens.main.mashi.trait

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

/* ---------------------------------- */
/* Color matrices */
/* ---------------------------------- */

val maskingMatrix = ColorMatrix(
    floatArrayOf(
        1f, 0f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f, 0f,
        0f, 0f, 1f, 0f, 0f,
        0f, 0f, 0f, 100f, -10f
    )
)

val commonMatrix = ColorMatrix()

@Composable
private fun NonSvgTrait(
    modifier: Modifier,
    onClick: () -> Unit,
    width: Dp,
    height: Dp,
    data: String,
    background: Color,
    contentScale: ContentScale
) {
    val ctx = LocalContext.current
    val staticLoader = remember(ctx) {
        ImageLoader.Builder(ctx)
            .components {
                add(AnimatedImageDecoder.Factory())
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
            .width(width)
            .height(height)
            .clip(MashiHolderShape)
            .background(background)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = request,
            imageLoader = staticLoader,
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = contentScale
        )
    }
}

@Composable
private fun SvgTrait(
    modifier: Modifier,
    onClick: () -> Unit,
    width: Dp,
    height: Dp,
    data: String,
    background: Color,
    selectedColors: SelectedColors?,
    contentScale: ContentScale
) {
    val ctx = LocalContext.current

    var cachedPainter by remember { mutableStateOf<Painter?>(null) }
    var hasMask by remember { mutableStateOf(false) }

    val svgLoader = remember(ctx, selectedColors) {
        ImageLoader.Builder(ctx)
            .components {
                add(
                    SvgCustomDecoderFactory(
                        selectedColors = selectedColors,
                        onMaskDetection = { hasMask = it }
                    )
                )
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
            .width(width)
            .height(height)
            .clip(MashiHolderShape)
            .background(background)
            .clickable(onClick = onClick)
    ) {
        // Cached painter overlay
        cachedPainter?.let {
            Image(
                painter = it,
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = contentScale,
                colorFilter = ColorFilter.colorMatrix(
                    if (hasMask) maskingMatrix else commonMatrix
                )
            )
        }

        AsyncImage(
            model = request,
            imageLoader = svgLoader,
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = contentScale,
            colorFilter = if (cachedPainter != null)
                ColorFilter.colorMatrix(if (hasMask) maskingMatrix else commonMatrix)
            else null,
            onState = { state ->
                if (state is AsyncImagePainter.State.Success) {
                    cachedPainter = state.painter
                }
            }
        )
    }
}


@Composable
fun Trait(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    width: Dp = MashiHolderWidth,
    height: Dp = MashiHolderHeight,
    data: String,
    background: Color = MashiBackground,
    selectedColors: SelectedColors? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    val isSvg by rememberIsSvg(data)

    if (isSvg != null) {
        if (isSvg == true) {
            SvgTrait(
                modifier = modifier,
                onClick = onClick,
                width = width,
                height = height,
                data = data,
                background = background,
                selectedColors = selectedColors,
                contentScale = contentScale
            )
        } else {
            NonSvgTrait(
                modifier = modifier,
                onClick = onClick,
                width = width,
                height = height,
                data = data,
                background = background,
                contentScale = contentScale
            )
        }
    }
}

@Composable
fun rememberIsSvg(url: String): State<Boolean?> {
    // Reset to null whenever URL changes
    val result = remember(url) { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(url) {
        val isSvg = withContext(Dispatchers.IO) {
            try {
                val connection = (URL(url).openConnection() as HttpURLConnection).apply {
                    requestMethod = "HEAD"
                    connectTimeout = 5000
                    readTimeout = 5000
                }
                connection.connect()
                val contentType = connection.contentType
                connection.disconnect()
                contentType?.contains("svg", ignoreCase = true) == true
            } catch (e: Exception) {
                false
            }
        }
        result.value = isSvg
    }

    return result
}