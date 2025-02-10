package com.example.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.provider.Settings
import androidx.core.app.NotificationCompat
import kotlin.math.abs
import kotlin.random.Random

class Notification(private val context: Context) {

    /**
     * This [sendNotification] helps us to send notification.
     */
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
     fun sendNotification( title: String, body: String) {
        val notification = NotificationCompat.Builder(context, "200")  // This variable is holding the builder of notification
            .setSmallIcon(R.drawable.location)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()

        notificationManager.apply {
            createNotificationChannel(
                NotificationChannel(
                    "200",
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
            notify(abs(Random.nextInt()), notification)
        }
    }
}