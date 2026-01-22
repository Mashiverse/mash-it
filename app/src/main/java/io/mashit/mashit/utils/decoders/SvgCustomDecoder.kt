package io.mashit.mashit.utils.decoders

import coil3.ImageLoader
import coil3.decode.DecodeResult
import coil3.decode.Decoder
import coil3.fetch.SourceFetchResult
import coil3.request.Options
import coil3.svg.SvgDecoder
import io.mashit.mashit.data.models.color.SelectedColors
import io.mashit.mashit.utils.color.helpers.replaceColors
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
) : Decoder {
    override suspend fun decode(): DecodeResult? {
        var svgText = result.source.source().readUtf8()
        if (selectedColors != null) {
            svgText = replaceColors(
                svgText,
                selectedColors.base,
                selectedColors.eyes,
                selectedColors.hair
            )
        }

        onMaskDetection.invoke(svgText.containsSvgMask())

        val editedResult = SourceFetchResult(
            source = coil3.decode.ImageSource(
                Buffer().writeUtf8(svgText),
                result.source.fileSystem
            ),
            mimeType = "image/svg+xml",
            dataSource = result.dataSource
        )

        val svgDecoder = SvgDecoder.Factory().create(editedResult, options, imageLoader)
            ?: throw IllegalArgumentException("Could not create SvgDecoder")

        return svgDecoder.decode()
    }

    class Factory(
        private val onMaskDetection: (Boolean) -> Unit,
        private val selectedColors: SelectedColors?,
    ) : Decoder.Factory {

        override fun create(
            result: SourceFetchResult,
            options: Options,
            imageLoader: ImageLoader
        ): Decoder {
            return SvgCustomDecoder(
                selectedColors = selectedColors,
                onMaskDetection = onMaskDetection,
                result = result,
                options = options,
                imageLoader = imageLoader
            )
        }
    }
}