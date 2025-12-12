package com.example.task_manager_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task_manager_app.data.TaskRepository
import com.example.task_manager_app.model.Task
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(
    private val repository: TaskRepository = TaskRepository()
) : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks
    private val _activeTasks = MutableLiveData<List<Task>>()
    val activeTasks: LiveData<List<Task>> = _activeTasks

    private val _doneTasks = MutableLiveData<List<Task>>()
    val doneTasks: LiveData<List<Task>> = _doneTasks
    private var allTasks: List<Task> = emptyList()
    private var selectedDate: LocalDate = LocalDate.now()

    fun loadTasks() {
        viewModelScope.launch {
            allTasks = repository.loadTasks()
            applyFilter()
        }
    }

    fun selectDate(date: LocalDate) {
        selectedDate = date
        applyFilter()
    }

    private fun applyFilter() {
        val tasksForDay = allTasks.filter { it.date == selectedDate }

        _activeTasks.value = tasksForDay
            .filter { !it.done }
            .sortedBy { it.time }

        _doneTasks.value = tasksForDay
            .filter { it.done }
            .sortedBy { it.time }
    }

}
