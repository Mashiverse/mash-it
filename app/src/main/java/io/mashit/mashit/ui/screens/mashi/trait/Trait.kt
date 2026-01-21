package io.mashit.mashit.ui.screens.mashi.trait

import ApngImage
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
import java.io.InputStream
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
private fun AnimatedImage(
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
private fun SvgImage(
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

enum class ImageType {
    SVG,
    APNG,
    OTHER
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
    val imageType by rememberContentType(data)

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
            if (imageType == ImageType.SVG) {
                SvgImage(
                    modifier = modifier,
                    data = data,
                    selectedColors = selectedColors,
                    contentScale = contentScale
                )
            } else if(
                imageType == ImageType.APNG
            ) {
                ApngImage(
                    url = data,
                    modifier = modifier
                )
            } else {
                AnimatedImage(
                    modifier = modifier,
                    data = data,
                    contentScale = contentScale
                )
            }
        }
    }
}



@Composable
fun rememberContentType(url: String): State<ImageType?> {
    val result = remember(url) { mutableStateOf<ImageType?>(null) }

    LaunchedEffect(url) {
        val type = withContext(Dispatchers.IO) {
            svgCheckSemaphore.withPermit {
                try {
                    val connection = (URL(url).openConnection() as HttpURLConnection).apply {
                        requestMethod = "GET" // GET to read first bytes
                        connectTimeout = 5000
                        readTimeout = 5000
                    }

                    connection.inputStream.use { stream ->
                        detectImageType(stream)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    ImageType.OTHER
                }
            }
        }
        result.value = type
    }

    return result
}

/** Detects SVG, APNG, or OTHER from the first 1000 bytes */
private fun detectImageType(input: InputStream): ImageType {
    val buffer = ByteArray(1000)
    val bytesRead = input.read(buffer, 0, buffer.size)
    if (bytesRead <= 0) return ImageType.OTHER

    val header = buffer.copyOfRange(0, bytesRead).toString(Charsets.US_ASCII).lowercase()

    // Quick SVG detection: check for <svg or <?xml at the start
    if (header.contains("<svg") || header.contains("<?xml")) {
        return ImageType.SVG
    }

    // PNG signature: 89 50 4E 47 0D 0A 1A 0A
    val pngSignature = byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)
    if (bytesRead >= 8 && buffer.copyOfRange(0, 8).contentEquals(pngSignature)) {
        // APNG detection: look for acTL chunk
        val acTL = "acTL".toByteArray(Charsets.US_ASCII)
        for (i in 8 until bytesRead - 3) {
            if (buffer.sliceArray(i..i + 3).contentEquals(acTL)) return ImageType.APNG
        }
        return ImageType.OTHER // PNG but not animated (could be treated differently if needed)
    }

    return ImageType.OTHER
}