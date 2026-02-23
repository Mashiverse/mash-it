package com.mashiverse.mashit.sys.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mashiverse.mashit.R

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Extract data from the FCM 'data' payload
        // Note: Use data payload primarily to ensure onMessageReceived triggers in background
        val listingId = remoteMessage.data["listingId"]

        // Use notification payload if available, otherwise fallback to data or defaults
        val title =
            remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "New Mashup!"
        val body = remoteMessage.notification?.body ?: remoteMessage.data["body"]
        ?: "Check out this shop item."

        sendNotification(title, body, listingId)
    }

    private fun sendNotification(title: String, body: String, listingId: String?) {
        val channelId = "shop_notifications"
        val context = applicationContext

        // 1. Build the App Link URI (Migrated from mashit:// to https://)
        val uri = if (!listingId.isNullOrBlank()) {
            "https://www.mash-it.io/shop/$listingId".toUri()
        } else {
            "https://www.mash-it.io/shop".toUri()
        }

        // 2. Create the Intent
        // Using ACTION_VIEW ensures the system treats this as a deep link/app link
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            `package` = context.packageName // Ensures only your app handles this intent
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 3. Setup Notification Channel for API 26+
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.getNotificationChannel(channelId) == null) {
            val channel = NotificationChannel(
                channelId,
                "Shop Updates",
                NotificationManager.IMPORTANCE_HIGH // HIGH shows heads-up notification
            ).apply {
                description = "Notifications for new shop listings"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // 4. Build the Notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}