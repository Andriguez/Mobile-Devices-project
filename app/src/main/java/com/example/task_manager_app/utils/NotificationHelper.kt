package com.example.task_manager_app.utils

import android.Manifest
import com.example.task_manager_app.R
import android.app.AlarmManager
import java.time.LocalDateTime
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.task_manager_app.ui.main.MainActivity
import java.time.ZoneId
import java.time.LocalDate
import java.time.LocalTime

/**
 * Helper class for managing notifications.
 * Handles notification channel creation and notification display.
 */
class NotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "task_reminders"
        private const val CHANNEL_NAME = "Task Reminders"
        private const val CHANNEL_DESCRIPTION = "Notifications for task reminders"
        private const val NOTIFICATION_ID = 1001
    }

    init {
        createNotificationChannel()
    }

    /**
     * Creates a notification channel for Android O and above.
     * Required for showing notifications on API 26+.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Shows a notification for a task reminder.
     * Checks for notification permission on API 33+.
     */
    fun showTaskReminder(title: String, message: String) {
        // Check permission for API 33+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        // Create intent for when notification is tapped
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Build notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Show notification
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    /**
     * Shows a simple test notification.
     * Used to demonstrate notification functionality.
     */
    // Schedule notification 5 minutes before task time
    fun scheduleTaskReminder5MinBefore(
        title: String,
        description: String,
        date: LocalDate,
        time: LocalTime
    ) {
        val taskDateTime = LocalDateTime.of(date, time)
        val notifyDateTime = taskDateTime.minusMinutes(5)

        val triggerMillis = notifyDateTime
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        if (triggerMillis <= System.currentTimeMillis()) return

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("taskTitle", title)
            putExtra("taskDescription", description)
        }

        val pending = PendingIntent.getBroadcast(
            context,
            0,  // use unique task id
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerMillis,
            pending
        )
    }

    fun showTestNotification() {
        showTaskReminder(
            title = context.getString(com.example.task_manager_app.R.string.notification_test_title),
            message = context.getString(com.example.task_manager_app.R.string.notification_test_message)
        )
    }
}