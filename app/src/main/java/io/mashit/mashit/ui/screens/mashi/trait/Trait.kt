package io.mashit.mashit.ui.screens.mashi.trait

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.gif.AnimatedImageDecoder
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import io.mashit.mashit.data.models.color.SelectedColors
import io.mashit.mashit.ui.theme.MashiBackground
import io.mashit.mashit.ui.theme.MashiHolderShape
import io.mashit.mashit.utils.decoders.SvgCustomDecoderFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

private val svgCheckSemaphore = Semaphore(10)

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
    data: String,
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

    AsyncImage(
        model = request,
        imageLoader = staticLoader,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
    )
}

@Composable
private fun SvgTrait(
    modifier: Modifier,
    data: String,
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
            .clip(MashiHolderShape)
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
            colorFilter = ColorFilter.colorMatrix(if (hasMask) maskingMatrix else commonMatrix),
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
    onClick: (() -> Unit)? = null,

    data: String,
    background: Color = MashiBackground,
    selectedColors: SelectedColors? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    val isSvg by rememberIsSvg(data)

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
        if (isSvg != null) {

            if (isSvg == true) {
                SvgTrait(
                    modifier = modifier,
                    data = data,
                    selectedColors = selectedColors,
                    contentScale = contentScale
                )
            } else {
                NonSvgTrait(
                    modifier = modifier,
                    data = data,
                    contentScale = contentScale
                )
            }
        }
    }
}

@Composable
fun rememberIsSvg(url: String): State<Boolean?> {
    val result = remember(url) { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(url) {
        val isSvg = withContext(Dispatchers.IO) {
            svgCheckSemaphore.withPermit {
                try {
                    val connection = (URL(url).openConnection() as HttpURLConnection).apply {
                        requestMethod = "HEAD"
                        connectTimeout = 100
                        readTimeout = 100
                    }
                    connection.connect()
                    val contentType = connection.contentType
                    connection.disconnect()
                    contentType?.contains("svg", ignoreCase = true) == true
                } catch (e: Exception) {
                    false
                }
            }
        }
        result.value = isSvg
    }

    return result
}