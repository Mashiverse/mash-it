package com.mashiverse.mashit.utils.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mashiverse.mashit.R

object NotificationHelper {
    private const val CHANNEL_ID = "upload_channel"

    fun createDownloadNotification(ctx: Context, title: String = "Generating...", message: String = ""): android.app.Notification {
        val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Upload Status",
            NotificationManager.IMPORTANCE_LOW
        ).apply { description = "Status of image generations" }
        notificationManager.createNotificationChannel(channel)

        return NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    fun showNotification(ctx: Context, title: String, message: String, imageUri: Uri? = null) {
        val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 1. Create the Intent to view the image
        val viewIntent = imageUri?.let {
            Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(it, "image/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }

        // 2. Wrap it in a PendingIntent
        val pendingIntent = viewIntent?.let {
            PendingIntent.getActivity(
                ctx,
                0,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notification = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .apply {
                // 3. Attach the intent to the notification body
                if (pendingIntent != null) setContentIntent(pendingIntent)
            }
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}