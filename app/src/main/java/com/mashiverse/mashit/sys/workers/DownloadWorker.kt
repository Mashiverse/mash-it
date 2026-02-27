package com.mashiverse.mashit.sys.workers

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mashiverse.mashit.data.repos.MashiverseRepo
import com.mashiverse.mashit.utils.helpers.ImageHelper
import com.mashiverse.mashit.utils.helpers.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class DownloadWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted params: WorkerParameters,
    val mashiverseRepo: MashiverseRepo
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setForeground(getForegroundInfo())
        }

        return withContext(Dispatchers.IO) {
            try {
                val wallet = inputData.getString(WALLET) ?: return@withContext Result.failure()
                val imgType = inputData.getInt(IMG_TYPE, 0)

                // 1. Fetch data
                val mashupResult = mashiverseRepo.getMashup(wallet, imgType)

                val timestamp = System.currentTimeMillis()
                val fileName = if (mashupResult.contentType == "image/png") {
                    "mashup_$timestamp.png"
                } else {
                    "mashup_$timestamp.gif"
                }

                // 2. Save image and CAPTURE the Uri
                // Note: Ensure ImageHelper.saveImageToGallery returns a Uri?
                val savedUri = ImageHelper.saveImageToGallery(
                    context = applicationContext,
                    imageBytes = mashupResult.bytes,
                    fileName = fileName,
                    mimeType = mashupResult.contentType
                )

                // 3. Pass Uri to notification
                NotificationHelper.showNotification(
                    ctx = applicationContext,
                    title = "Image saved to Gallery",
                    message = "Tap to view $fileName",
                    imageUri = savedUri
                )

                Result.success()

            } catch (e: Exception) {
                if (runAttemptCount < 3) {
                    Result.retry()
                } else {
                    NotificationHelper.showNotification(
                        applicationContext,
                        "Generation failed",
                        "Error: ${e.message}"
                    )
                    Result.failure()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun getForegroundInfo(): androidx.work.ForegroundInfo {
        return androidx.work.ForegroundInfo(
            1001,
            NotificationHelper.createDownloadNotification(applicationContext),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
        )
    }

    companion object {
        const val WALLET = "wallet"
        const val IMG_TYPE = "img_type"
    }
}