package com.mashiverse.mashit.sys.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mashiverse.mashit.data.repos.MashiverseRepo
import com.mashiverse.mashit.utils.helpers.saveImageToGallery
import com.mashiverse.mashit.utils.helpers.sys.showNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    val mashiverseRepo: MashiverseRepo
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val wallet = inputData.getString(WALLET)
            val imgType = inputData.getInt(IMG_TYPE, 0)

            wallet?.let {
                val mashupResult = mashiverseRepo.getMashup(wallet, imgType)
                val timestamp = System.currentTimeMillis()
                val fileName = if (mashupResult.contentType == "image/png") {
                    "mashup_$timestamp.png"
                } else {
                    "mashup_$timestamp.gif"
                }

                saveImageToGallery(
                    context = applicationContext,
                    imageBytes = mashupResult.bytes,
                    fileName = fileName,
                    mimeType = mashupResult.contentType
                )

                showNotification(applicationContext, "Image saved", fileName)
            }
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                showNotification(
                    applicationContext,
                    "Download failed",
                    "Error: ${e.message}"
                )

                Result.failure()
            }
        }
    }

    companion object {
        const val WALLET = "wallet"
        const val IMG_TYPE = "img_type"
    }
}