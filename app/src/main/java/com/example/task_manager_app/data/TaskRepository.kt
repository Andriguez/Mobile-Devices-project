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
                Task(
                    1,
                    "Acheter du lait",
                    "Supermarché, rayon lait",
                    LocalDate.now(),
                    LocalTime.of(9, 30)
                ),
                Task(
                    2,
                    "Appeler Alice",
                    "Rappeler au sujet du projet",
                    LocalDate.now(),
                    LocalTime.of(14, 0)
                ),
                Task(
                    3,
                    "Envoyer rapport",
                    "PDF à joindre et envoyer au manager",
                    LocalDate.now().plusDays(1),
                    LocalTime.of(11, 0)
                ),
                Task(
                    3,
                    "Other task",
                    "do something else",
                    LocalDate.now().plusDays(2),
                    LocalTime.of(14, 0)
                ),

                Task(
                    1,
                    "Done task 1",
                    "Supermarché, rayon lait",
                    LocalDate.now(),
                    LocalTime.of(12, 30),
                    true
                ),

                Task(
                    1,
                    "Done task 2",
                    "Supermarché, rayon lait",
                    LocalDate.now(),
                    LocalTime.of(10, 45),
                    true
                )

            )
        }
}