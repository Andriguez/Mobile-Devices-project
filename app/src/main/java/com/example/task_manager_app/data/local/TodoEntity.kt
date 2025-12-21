package com.example.task_manager_app.data.local
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a todo task in the database.
*/
@Entity(tableName = "todo")
class TodoEntity(priority: Int, title: String, description: String, dueDate: Long?) {

@PrimaryKey(autoGenerate = true)
val id: Long = 0

val title: String = ""
val description: String = ""
val isCompleted: Boolean = false
var priority: Int = 2 // 1=Low, 2=Medium, 3=High
val createdAt: Long = System.currentTimeMillis()
val dueDate: Long? = null
}