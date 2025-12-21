package com.example.task_manager_app.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("taskTitle") ?: "Task reminder"
        val description = intent.getStringExtra("taskDescription") ?: ""
        NotificationHelper(context).showTaskReminder(title, description)
    }
}