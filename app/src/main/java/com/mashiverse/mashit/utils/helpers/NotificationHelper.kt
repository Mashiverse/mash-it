package com.mashiverse.mashit.utils.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.mashiverse.mashit.R

object NotificationHelper {
    private const val CHANNEL_ID = "upload_channel"

    // This creates the Notification object WITHOUT showing it immediately.
    // Required for Worker.getForegroundInfo()
    fun createDownloadNotification(ctx: Context, title: String = "Generating...", message: String = ""): android.app.Notification {
        val notificationManager =
            ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create Channel (Safe to call multiple times)
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Upload Status",
            NotificationManager.IMPORTANCE_LOW // Use LOW so it doesn't "pop" or beep constantly
        ).apply {
            description = "Status of image generations"
        }
        notificationManager.createNotificationChannel(channel)

        return NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_LOW) // Good for background tasks
            .setOngoing(true) // Suggests this is an active task
            .build()
    }

    // Your existing helper to "fire and forget" a notification
    fun showNotification(ctx: Context, title: String, message: String) {
        val notificationManager =
            ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Reuse the builder logic to keep styles consistent
        val notification = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}