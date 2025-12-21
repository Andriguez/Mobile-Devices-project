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
import java.time.LocalTime

class TaskViewModel(
    private val repository: TaskRepository = TaskRepository(),
    private val holidayRepository: HolidayRepository = HolidayRepository(),
    private val _openCalendarEvent: MutableLiveData<Task> = MutableLiveData<Task>(),
    val openCalendarEvent: LiveData<Task> = _openCalendarEvent
) : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks
    private val _activeTasks = MutableLiveData<List<Task>>()
    val activeTasks: LiveData<List<Task>> = _activeTasks

    private val _doneTasks = MutableLiveData<List<Task>>()
    val doneTasks: LiveData<List<Task>> = _doneTasks

    // LiveData des dates de jours fériés (Set)
    private val _holidays = MutableLiveData<Set<LocalDate>>(emptySet())
    val holidays: LiveData<Set<LocalDate>> = _holidays

    // map date -> name (vide pour l'instant, compatible avec UI qui attend holidayNames)
    private val _holidayNames = MutableLiveData<Map<LocalDate, String>>(emptyMap())
    val holidayNames: LiveData<Map<LocalDate, String>> = _holidayNames

    private var allTasks: List<Task> = emptyList()
    private var selectedDate: LocalDate = LocalDate.now()
    private val _selectedHolidayName = MutableLiveData<String?>()
    val selectedHolidayName: LiveData<String?> = _selectedHolidayName


    fun loadTasks() {
        viewModelScope.launch {
            allTasks = repository.loadTasks()
            applyFilter()
        }
    }

    fun selectDate(date: LocalDate) {
        selectedDate = date
        applyFilter()

        val name = holidayNames.value?.get(date)
        _selectedHolidayName.value = name
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

    fun addTask(
        title: String,
        description: String,
        dateStr: String,
        timeStr: String,
        done: Boolean
    ) {
        val date = LocalDate.parse(dateStr)
        val time = LocalTime.parse(timeStr)
        val nextId = (allTasks.maxOfOrNull { it.id } ?: 0) + 1
        val newTask = Task(nextId, title, description, date, time, done)
        allTasks = allTasks + newTask
        applyFilter()
    }

    fun editTask(
        id: Int,
        title: String,
        description: String,
        dateStr: String,
        timeStr: String,
        done: Boolean
    ) {
        val date = LocalDate.parse(dateStr)
        val time = LocalTime.parse(timeStr)
        val updated = Task(id, title, description, date, time, done)
        allTasks = allTasks.map { if (it.id == id) updated else it }
        applyFilter()
    }

    fun deleteTask(id: Int) {
        allTasks = allTasks.filter { it.id != id }
        applyFilter()
    }

    // charge les jours fériés pour une année (HolidayRepository renvoie Set<LocalDate>)
    fun loadHolidays(year: Int = LocalDate.now().year, countryCode: String = "FR") {
        viewModelScope.launch {
            try {
                val map = holidayRepository.getPublicHolidaysWithNames(year, countryCode)
                _holidays.value = map.keys.toSet()
                _holidayNames.value = map
            } catch (_: Exception) {
                // ignore
            }
        }
    }

    // charge pour plusieurs années et fusionne les ensembles
    fun loadHolidaysForYears(years: Set<Int>, countryCode: String = "FR") {
        viewModelScope.launch {
            val mergedSet = mutableSetOf<LocalDate>()
            val mergedNames = mutableMapOf<LocalDate, String>()
            for (y in years) {
                try {
                    val map = holidayRepository.getPublicHolidaysWithNames(y, countryCode)
                    mergedSet.addAll(map.keys)
                    mergedNames.putAll(map)
                } catch (_: Exception) {
                    // protège contre une année qui échoue
                }
            }
            _holidays.value = mergedSet
            _holidayNames.value = mergedNames
        }
    }

    fun requestOpenCalendar(task: Task) {
        _openCalendarEvent.value = task
    }
}
