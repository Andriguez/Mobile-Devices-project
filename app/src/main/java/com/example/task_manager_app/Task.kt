package com.example.task_manager_app

import java.time.LocalDate
import java.time.LocalTime

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val date: LocalDate,
    val time: LocalTime,
    var done: Boolean = false
)
