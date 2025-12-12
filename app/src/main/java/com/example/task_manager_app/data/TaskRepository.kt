package com.example.task_manager_app.data

import com.example.task_manager_app.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime

class TaskRepository {
    suspend fun loadTasks(): List<Task> =
        withContext(Dispatchers.IO) {
            delay(500) // simulate IO
            listOf(
                Task(1, "Acheter du lait", "...", LocalDate.now(), LocalTime.of(9, 30)),
                Task(2, "Appeler Alice", "...", LocalDate.now(), LocalTime.of(14, 0)),
                Task(3, "Envoyer rapport", "...", LocalDate.now().plusDays(1), LocalTime.of(11, 0))
            )
        }
}