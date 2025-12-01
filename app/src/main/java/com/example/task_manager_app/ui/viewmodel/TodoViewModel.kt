package com.example.task_manager_app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.task_manager_app.data.local.TodoDatabase
import com.example.task_manager_app.data.local.TodoEntity
import com.example.task_manager_app.data.repository.TodoRepository

import kotlinx.coroutines.launch

/**
 * ViewModel for managing todo operations and UI state.
 */
class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TodoRepository

    val allTodos: LiveData<List<TodoEntity>>
    val incompleteTodos: LiveData<List<TodoEntity>>
    val completedTodos: LiveData<List<TodoEntity>>

    init {
        val todoDao = TodoDatabase.getDatabase(application).todoDao()
        repository = TodoRepository(todoDao)
        allTodos = repository.allTodos
        incompleteTodos = repository.incompleteTodos
        completedTodos = repository.completedTodos
    }

    fun insert(todo: TodoEntity) = viewModelScope.launch {
        repository.insert(todo)
    }

    fun update(todo: TodoEntity) = viewModelScope.launch {
        repository.update(todo)
    }

    fun delete(todo: TodoEntity) = viewModelScope.launch {
        repository.delete(todo)
    }

    fun toggleCompletion(todo: TodoEntity) = viewModelScope.launch {
        repository.toggleCompletion(todo.id, !todo.isCompleted)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun createTodo(
        title: String,
        description: String = "",
        priority: Int = 2,
        dueDate: Long? = null
    ) {
        val todo = TodoEntity(
            title = title,
            description = description,
            priority = priority,
            dueDate = dueDate
        )
        insert(todo)
    }
}