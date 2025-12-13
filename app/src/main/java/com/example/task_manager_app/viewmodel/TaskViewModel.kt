package com.example.task_manager_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task_manager_app.data.HolidayRepository
import com.example.task_manager_app.data.TaskRepository
import com.example.task_manager_app.model.Task
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(
    private val repository: TaskRepository = TaskRepository(),
    private val holidayRepository: HolidayRepository = HolidayRepository()

) : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks
    private val _activeTasks = MutableLiveData<List<Task>>()
    val activeTasks: LiveData<List<Task>> = _activeTasks

    private val _doneTasks = MutableLiveData<List<Task>>()
    val doneTasks: LiveData<List<Task>> = _doneTasks

    private val _holidays = MutableLiveData<Set<LocalDate>>(emptySet())
    val holidays: LiveData<Set<LocalDate>> = _holidays

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

    //load for one year
    fun loadHolidays(year: Int = LocalDate.now().year, countryCode: String = "FR") {
        viewModelScope.launch {
            val set = holidayRepository.getPublicHolidays(year, countryCode)
            _holidays.value = set
        }
    }

    //load for multiple years
    fun loadHolidaysForYears(years: Set<Int>, countryCode: String = "FR") {
        viewModelScope.launch {
            val merged = mutableSetOf<LocalDate>()
            for (y in years) {
                try {
                    val set = holidayRepository.getPublicHolidays(y, countryCode)
                    merged.addAll(set)
                } catch (_: Exception) {
                    // protège contre une année qui échoue
                }
            }
            _holidays.value = merged
        }
    }
}
