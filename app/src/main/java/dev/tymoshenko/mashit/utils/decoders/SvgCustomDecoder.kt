package dev.tymoshenko.mashit.utils.decoders

import coil3.ImageLoader
import coil3.decode.DecodeResult
import coil3.decode.Decoder
import coil3.fetch.SourceFetchResult
import coil3.request.Options
import coil3.svg.SvgDecoder
import dev.tymoshenko.mashit.data.models.color.SelectedColors
import dev.tymoshenko.mashit.utils.color.helpers.replaceColors
import okio.Buffer

fun String.containsSvgMask(): Boolean {
    // Matches "<mask" followed by any characters until ">"
    val maskRegex = Regex("<mask[\\s>]", RegexOption.IGNORE_CASE)
    return maskRegex.containsMatchIn(this)
}

class SvgCustomDecoder(
    private val onMaskDetection: (Boolean) -> Unit,
    private val selectedColors: SelectedColors?,
    private val result: SourceFetchResult,
    private val options: Options,
    private val imageLoader: ImageLoader,
    private val onSvgDetection: (Boolean) -> Unit,
) : Decoder {

    override suspend fun decode(): DecodeResult? {
        // Read the full SVG text
        var svgText = result.source.source().readUtf8()
        onSvgDetection.invoke(true)

        // Replace colors
        if (selectedColors != null) {
            svgText = replaceColors(
                svgText,
                selectedColors.body,
                selectedColors.eyes,
                selectedColors.hair
            )
        }

        // Mask detection callback
        onMaskDetection.invoke(svgText.containsSvgMask())

        // Wrap into a new SourceFetchResult
        val editedResult = SourceFetchResult(
            source = coil3.decode.ImageSource(
                Buffer().writeUtf8(svgText),
                result.source.fileSystem
            ),
            mimeType = "image/svg+xml",
            dataSource = result.dataSource
        )

        // Delegate decoding to Coil's built-in SvgDecoder
        val svgDecoder = SvgDecoder.Factory().create(editedResult, options, imageLoader)
            ?: throw IllegalArgumentException("Could not create SvgDecoder")

        return svgDecoder.decode()
    }
}

class SvgCustomDecoderFactory(
    private val onMaskDetection: (Boolean) -> Unit,
    private val selectedColors: SelectedColors?,
    private val onSvgDetection: (Boolean) -> Unit,
) : Decoder.Factory {

    override fun create(
        result: SourceFetchResult,
        options: Options,
        imageLoader: ImageLoader
    ): Decoder? {
        if (result.mimeType != null && !result.mimeType!!.contains("svg", ignoreCase = true)) {
            onSvgDetection.invoke(false)
            onMaskDetection.invoke(false)
            return null
        }

        return SvgCustomDecoder(
            selectedColors = selectedColors,
            onMaskDetection = onMaskDetection,
            result = result,
            options = options,
            imageLoader = imageLoader,
            onSvgDetection = onSvgDetection
        )
    }
}