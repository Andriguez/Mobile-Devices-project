// File: `app/src/main/java/com/example/task_manager_app/utils/DaysGenerator.kt`
package com.example.task_manager_app.utils

import com.example.task_manager_app.model.DayItem
import java.time.LocalDate
import kotlin.math.max

fun generateDayItems(start: LocalDate, count: Int): List<DayItem> {
    val safeCount = max(0, count)
    return (0 until safeCount).map { DayItem(start.plusDays(it.toLong())) }
}




