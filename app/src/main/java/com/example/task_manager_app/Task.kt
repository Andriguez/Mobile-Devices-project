package com.example.task_manager_app

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    var done: Boolean = false
)
