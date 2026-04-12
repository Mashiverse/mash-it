package com.mashiverse.mashit.utils.helpers.nft

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import com.mashiverse.mashit.data.models.sys.image.ImageType
import java.io.InputStream

fun detectImageType(input: InputStream): ImageType {
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

fun saveImageToGallery(
    context: Context,
    imageBytes: ByteArray,
    fileName: String,
    mimeType: String
) {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let {
        resolver.openOutputStream(it)?.use { outputStream ->
            outputStream.write(imageBytes)
            outputStream.flush()
        }
    }
}

fun String.fromIpfsScheme() = this.replace("ipfs://", "https://ipfs.filebase.io/ipfs/")

fun String.toIpfsUri() = this.replace("https://ipfs.filebase.","https://ipfs.")

fun String.toFilebaseUri() = this.replace("https://ipfs.", "https://ipfs.filebase.")