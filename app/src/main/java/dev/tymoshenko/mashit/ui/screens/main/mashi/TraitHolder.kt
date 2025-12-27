package dev.tymoshenko.mashit.ui.screens.main.mashi

import android.graphics.drawable.Drawable
import android.util.Log
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
import coil3.decode.DecodeResult
import coil3.decode.Decoder
import coil3.decode.ImageSource
import coil3.fetch.SourceFetchResult
import coil3.gif.AnimatedImageDecoder
import coil3.request.ImageRequest
import coil3.request.Options
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import dev.tymoshenko.mashit.ui.theme.MashiBackground
import dev.tymoshenko.mashit.ui.theme.MashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.MashiHolderShape
import dev.tymoshenko.mashit.ui.theme.MashiHolderWidth
import dev.tymoshenko.mashit.utils.color.helpers.replaceColors
import okio.Buffer


class SvgColorReplacementDecoder(
    private val hair: String,
    private val eyes: String,
    private val body: String,
    private val result: SourceFetchResult,
    private val options: Options,
    private val imageLoader: ImageLoader
) : Decoder {

    override suspend fun decode(): DecodeResult? {
        // Read the full SVG text
        val svgText = result.source.source().readUtf8()

        // Replace colors
        val editedSvg = replaceColors(svgText, body, eyes, hair)

        // Wrap into a new SourceFetchResult
        val editedResult = SourceFetchResult(
            source = coil3.decode.ImageSource(Buffer().writeUtf8(editedSvg), result.source.fileSystem),
            mimeType = "image/svg+xml",
            dataSource = result.dataSource
        )

        // Delegate decoding to Coil's built-in SvgDecoder
        val svgDecoder = SvgDecoder.Factory().create(editedResult, options, imageLoader)
            ?: throw IllegalArgumentException("Could not create SvgDecoder")

        return svgDecoder.decode()
    }
}

class SvgColorReplacementDecoderFactory(
    private val hair: String,
    private val eyes: String,
    private val body: String
) : Decoder.Factory {

    override fun create(
        result: SourceFetchResult,
        options: Options,
        imageLoader: ImageLoader
    ): Decoder? {
        if (result.mimeType != null && !result.mimeType!!.contains("svg", ignoreCase = true)) {
            return null
        }

        return SvgColorReplacementDecoder(
            hair = hair,
            eyes = eyes,
            body = body,
            result = result,
            options = options,
            imageLoader = imageLoader
        )
    }
}

@Composable
fun TraitHolder(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    width: Dp = MashiHolderWidth,
    height: Dp = MashiHolderHeight,
    data: String = "https://example.com/image.svg",
    background: Color = MashiBackground,
    colors: Triple<String, String, String>? = null
) {
    val ctx = LocalContext.current

    val (body, eyes, hair) = colors ?: Triple("#00FF00", "#FFFF00", "#0000FF")
    val imageLoader = remember(colors) {
        ImageLoader.Builder(ctx)
            .components {
                add(SvgColorReplacementDecoderFactory(hair, eyes, body))
                add(AnimatedImageDecoder.Factory())
                add(SvgDecoder.Factory())
            }
            .build()
    }

    val request = ImageRequest.Builder(ctx)
        .data(data)
        .crossfade(true)
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
private fun TraitHolderPreview() {
    TraitHolder()
}