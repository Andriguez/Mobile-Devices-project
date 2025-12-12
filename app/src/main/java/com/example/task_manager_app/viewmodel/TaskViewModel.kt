package com.example.task_manager_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task_manager_app.data.TaskRepository
import com.example.task_manager_app.model.Task
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel : ViewModel() {

    private val _allTasks = MutableLiveData<List<Task>>()
    private val _tasks = MutableLiveData<List<Task>>()
    private val repository = TaskRepository()

    val tasks: LiveData<List<Task>> = _tasks

    fun loadTasks() {
        viewModelScope.launch {
            _tasks.value = repository.loadTasks()
        }
    }

    fun selectDate(date: LocalDate) {
        _tasks.value = _allTasks.value
            ?.filter { it.date == date }
            ?.sortedBy { it.time }
            ?: emptyList()
    }
}