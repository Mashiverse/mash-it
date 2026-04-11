package com.mashiverse.mashit.utils.helpers.sys

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.mashiverse.mashit.R

fun showNotification(ctx: Context, title: String, message: String) {
    val channelId = "upload_channel"
    val notificationManager =
        ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // 1. Create Channel (Required for Oreo+)
    val channel = NotificationChannel(
        channelId,
        "Upload Status",
        NotificationManager.IMPORTANCE_DEFAULT
    )
    notificationManager.createNotificationChannel(channel)

    // 2. Build the Notification
    val notification = NotificationCompat.Builder(ctx, channelId)
        .setContentTitle(title)
        .setContentText(message)
        .setSmallIcon(R.drawable.logo) // Make sure this icon exists
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    // 3. Show it
    notificationManager.notify(1, notification)
}