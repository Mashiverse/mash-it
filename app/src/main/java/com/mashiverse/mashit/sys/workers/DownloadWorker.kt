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
        // IMPORTANT: This promotes the worker to Foreground immediately.
        // On Android 12+, this is virtually instant; on older versions, it shows the notification.
        setForeground(getForegroundInfo())

        return withContext(Dispatchers.IO) {
            try {
                val wallet = inputData.getString(WALLET) ?: return@withContext Result.failure()
                val imgType = inputData.getInt(IMG_TYPE, 0)

                // 30s API Request
                val mashupResult = mashiverseRepo.getMashup(wallet, imgType)

                val timestamp = System.currentTimeMillis()
                val fileName = if (mashupResult.contentType == "image/png") {
                    "mashup_$timestamp.png"
                } else {
                    "mashup_$timestamp.gif"
                }

                ImageHelper.saveImageToGallery(
                    context = applicationContext,
                    imageBytes = mashupResult.bytes,
                    fileName = fileName,
                    mimeType = mashupResult.contentType
                )

                NotificationHelper.showNotification(
                    applicationContext,
                    "Image saved",
                    fileName
                )

                Result.success()

            } catch (e: Exception) {
                if (runAttemptCount < 3) {
                    Result.retry()
                } else {
                    NotificationHelper.showNotification(
                        applicationContext,
                        "Download failed",
                        "Error: ${e.message}"
                    )
                    Result.failure()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun getForegroundInfo(): androidx.work.ForegroundInfo {
        val notificationId = 1001
        return androidx.work.ForegroundInfo(
            notificationId,
            NotificationHelper.createDownloadNotification(applicationContext),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
        )
    }

    companion object {
        const val WALLET = "wallet"
        const val IMG_TYPE = "img_type"
    }
}