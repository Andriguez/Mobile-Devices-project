// app/src/main/java/com/example/task_manager_app/utils/DateExtensions.kt
package com.example.task_manager_app.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.toShortMonth(locale: Locale = Locale.getDefault()): String {
    val formatter = DateTimeFormatter.ofPattern("MMM", locale)
    return this.format(formatter)
}

// Utilisation (ex. dans un adapter / UI):
// tvMonth.text = dayItem.date.toShortMonth(Locale.FRANCE)
