package com.dicoding.listevent.ui.setting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dicoding.listevent.ApiConfig
import com.dicoding.listevent.ListEventsItem
import com.dicoding.listevent.R

class DailyReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        sendEventNotification()
        // Check if notifications are enabled
//        val settingsManager = SettingsManager(applicationContext)
//        if (settingsManager.isNotificationEnabled) {
//            // Fetch the closest event
//            val event = fetchClosestEvent()
//            event?.let {
//                showNotification(it.name, it.beginTime)
//            }
//        }
        return Result.success()
    }

    private suspend fun sendEventNotification() {
        val sharedPreference = context.getSharedPreferences("event_prefs", Context.MODE_PRIVATE)
        val isNotificationEnabled = sharedPreference.getBoolean("daily_reminder", false)

        if (isNotificationEnabled) {
            try {
                val event = fetchEvent()
                event?.let {
                    showNotification(it.name, it.beginTime)
                }
            } catch (e: Exception) {
                //handle exception
            }
        }
    }

    private suspend fun fetchEvent(): ListEventsItem? {
        val response = ApiConfig.getApiService().getClosestEvent(active = -1, limit = 1)
        if (response.isSuccessful) {
            val events = response.body()?.listEvents
            return events?.firstOrNull()
        }
        return null
    }

    private fun showNotification(eventName: String, eventTime: String) {
        val channelId = "EVENT_CHANNEL"

        // Create notification channel (required for Android Oreo and above)
//        val channel = NotificationChannel(
//            channelId,
//            "Event Reminder",
//            NotificationManager.IMPORTANCE_DEFAULT
//        ).apply {
//            description = "Reminder for upcoming events"
//        }
//        notificationManager.createNotificationChannel(channel)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, "Daily Event Reminder", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        // Build notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(eventName)
            .setContentText(eventTime)
            .setSmallIcon(R.drawable.notifi) // Replace with your appropriate icon
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
