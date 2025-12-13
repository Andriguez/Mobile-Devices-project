// kotlin
// File: app/src/main/java/com/example/task_manager_app/ui/landing/LandingActivity.kt
package com.example.task_manager_app.ui.landing

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task_manager_app.R
import com.example.task_manager_app.model.Task
import com.example.task_manager_app.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.LocalTime
import com.example.task_manager_app.utils.generateDayItems
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LandingActivity : AppCompatActivity() {

    private val viewModel: TaskViewModel by viewModels()
    private lateinit var addTaskLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.my_tasks)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TaskListFragment())
                .commit()
        }

        val daysRecycler = findViewById<RecyclerView>(R.id.recyclerDays)
        val days = generateDayItems(LocalDate.now(), 180)
        val dayAdapter = DayAdapter(days) { selectedDate ->
            viewModel.selectDate(selectedDate)
        }

        dayAdapter.setSelectedDate(LocalDate.now())

        viewModel.loadTasks()

        daysRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        daysRecycler.adapter = dayAdapter

        viewModel.holidays.observe(this) { set ->
            dayAdapter.setHolidays(set)
        }
        val years = days.map { it.date.year }.toSet()
        viewModel.loadHolidaysForYears(years, countryCode = "FR")

        addTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data ?: return@registerForActivityResult
                val title = data.getStringExtra("title") ?: return@registerForActivityResult
                val description = data.getStringExtra("description") ?: ""
                val dateStr = data.getStringExtra("date") ?: LocalDate.now().toString()
                val timeStr = data.getStringExtra("time") ?: LocalTime.now().toString()
                val done = data.getBooleanExtra("done", false)
                val id = data.getIntExtra("id", -1)
                if (id != -1) {
                    viewModel.editTask(id, title, description, dateStr, timeStr, done)
                } else {
                    viewModel.addTask(title, description, dateStr, timeStr, done)
                }
            }
        }

        val fab = findViewById<FloatingActionButton>(R.id.fabAddTask)
        fab.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            addTaskLauncher.launch(intent)
        }
    }

    // méthode publique pour ouvrir l'éditeur pour une tâche existante
    fun openEditTask(task: Task) {
        val intent = Intent(this, AddTaskActivity::class.java).apply {
            putExtra("id", task.id)
            putExtra("title", task.title)
            putExtra("description", task.description)
            putExtra("date", task.date.toString())
            putExtra("time", task.time.toString())
            putExtra("done", task.done)
        }
        addTaskLauncher.launch(intent)
    }


}
