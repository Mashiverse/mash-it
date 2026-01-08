package io.mashit.mashit.utils.io

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore

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