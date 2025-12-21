package com.example.task_manager_app.data

import android.content.Context
import com.example.task_manager_app.R
import com.example.task_manager_app.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime

class TaskRepository(private val context: Context) { // 1. Added Context here
    suspend fun loadTasks(): List<Task> =
        withContext(Dispatchers.IO) {
            delay(500)
            listOf(
                Task(
                    1,
                    context.getString(R.string.task_milk_title), // 2. Fetches English or French
                    context.getString(R.string.task_milk_desc),
                    LocalDate.now(),
                    LocalTime.of(9, 30)
                ),
                Task(
                    2,
                    context.getString(R.string.task_alice_title),
                    context.getString(R.string.task_alice_desc),
                    LocalDate.now(),
                    LocalTime.of(14, 0)
                ),
                // Repeat for other tasks...
                Task(
                    7,
                    context.getString(R.string.task_milk_title),
                    context.getString(R.string.task_milk_desc),
                    LocalDate.of(2025, 12, 25),
                    LocalTime.of(9, 30)
                )
            )
        }
}