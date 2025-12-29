package dev.tymoshenko.mashit.utils.decoders

import coil3.ImageLoader
import coil3.decode.DecodeResult
import coil3.decode.Decoder
import coil3.fetch.SourceFetchResult
import coil3.request.Options
import coil3.svg.SvgDecoder
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
            source = coil3.decode.ImageSource(
                Buffer().writeUtf8(editedSvg),
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