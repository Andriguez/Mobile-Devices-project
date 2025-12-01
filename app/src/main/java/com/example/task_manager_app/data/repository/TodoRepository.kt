package com.example.task_manager_app.data.repository

import androidx.lifecycle.LiveData
import com.example.task_manager_app.data.local.TodoDao
import com.example.task_manager_app.data.local.TodoEntity



/**
 * Repository class that provides a clean API for data access.
 */
class TodoRepository(private val todoDao: TodoDao) {

    val allTodos: LiveData<List<TodoEntity>> = todoDao.getAllTodos()
    val incompleteTodos: LiveData<List<TodoEntity>> = todoDao.getIncompleteTodos()
    val completedTodos: LiveData<List<TodoEntity>> = todoDao.getCompletedTodos()

    suspend fun insert(todo: TodoEntity): Long {
        return todoDao.insertTodo(todo)
    }

    suspend fun update(todo: TodoEntity) {
        todoDao.updateTodo(todo)
    }

    suspend fun delete(todo: TodoEntity) {
        todoDao.deleteTodo(todo)
    }

    suspend fun toggleCompletion(id: Long, isCompleted: Boolean) {
        todoDao.updateCompletionStatus(id, isCompleted)
    }

    suspend fun deleteAll() {
        todoDao.deleteAllTodos()
    }

    fun getTodoById(id: Long): LiveData<TodoEntity?> {
        return todoDao.getTodoById(id)
    }
}