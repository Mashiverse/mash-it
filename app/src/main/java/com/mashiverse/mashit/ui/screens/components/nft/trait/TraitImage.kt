package com.mashiverse.mashit.ui.screens.components.nft.trait

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
import com.mashiverse.mashit.data.models.color.SelectedColors
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.ui.theme.MashiBackground
import com.mashiverse.mashit.ui.theme.MashiHolderShape
import com.mashiverse.mashit.utils.MASHI_BASE_URL
import com.mashiverse.mashit.utils.decoders.SvgCustomDecoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

private val svgCheckSemaphore = Semaphore(10)


@Composable
private fun NonSvgImage(
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
                    SvgCustomDecoder.Factory(
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

@Composable
fun TraitImage(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    data: String,
    getImageType: (String) -> ImageType?,
    setImageType: (ImageType, String) -> Unit,
    background: Color = MashiBackground,
    selectedColors: SelectedColors? = null,
    contentScale: ContentScale = ContentScale.Fit
) {

    val imageType by rememberImageType(
        data = data,
        getImageType = { getImageType.invoke(data) },
        setImageType = setImageType
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
                        ImageType.SVG_MASK -> "${MASHI_BASE_URL}api/svg/${data.split("/").last()}"
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
                    val newData = "${MASHI_BASE_URL}api/apng/${data.split("/").last()}"
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


@Composable
private fun rememberImageType(
    data: String,
    getImageType: (String) -> ImageType?,
    setImageType: (ImageType, String) -> Unit
): State<ImageType?> {
    val result = remember(data) { mutableStateOf(getImageType.invoke(data)) }

    if (result.value != null) return result

    LaunchedEffect(data) {
        val type = withContext(Dispatchers.IO) {
            svgCheckSemaphore.withPermit {
                try {
                    val connection = (URL(data).openConnection() as HttpURLConnection).apply {
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
        setImageType.invoke(type, data)
    }

    return result
}

/** Detects SVG, APNG, or OTHER from the first 1000 bytes */
private fun detectImageType(input: InputStream): ImageType {
    // We use a buffered approach to peek at the start without losing data
    // or we read the bytes into a reusable array.
    val buffer = ByteArray(1024)
    input.mark(1024) // Mark the start if the stream supports it
    val bytesRead = input.read(buffer)
    if (bytesRead <= 0) return ImageType.OTHER

    // 1. PNG & APNG Detection (Check bytes first, it's faster)
    val pngSignature = byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)
    if (bytesRead >= 8 && buffer.take(8).toByteArray().contentEquals(pngSignature)) {
        // Look for "acTL" chunk in the first KB to identify Animated PNG
        val headerString = buffer.toString(Charsets.US_ASCII)
        return if (headerString.contains("acTL")) ImageType.APNG else ImageType.OTHER
    }

    // 2. SVG Detection (Read all if initial check suggests XML/SVG)
    val initialHeader = buffer.toString(Charsets.UTF_8).lowercase()
    if (initialHeader.contains("<svg") || initialHeader.contains("<?xml")) {
        // To be 100% sure it's a valid SVG, you might want to read the rest
        // Use .use { ... } to ensure the stream closes if this is the end of the line
        val fullContent = initialHeader + input.bufferedReader().use { it.readText() }
        if (fullContent.contains("mask", ignoreCase = true)) {
            return ImageType.SVG_MASK
        }

        return ImageType.SVG
    }

    return ImageType.OTHER
}