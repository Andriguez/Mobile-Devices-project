package com.example.task_manager_app.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task_manager_app.R
import com.example.task_manager_app.model.Task
import com.example.task_manager_app.ui.landing.LandingActivity
import com.example.task_manager_app.ui.readme.ReadmeActivity
import com.example.task_manager_app.ui.taskcreation.AddTaskActivity
import com.example.task_manager_app.utils.generateDayItems
import com.example.task_manager_app.viewmodel.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import com.example.task_manager_app.utils.NotificationHelper

class MainActivity : AppCompatActivity() {

    private val viewModel: TaskViewModel by viewModels()
    private lateinit var addTaskLauncher: ActivityResultLauncher<Intent>
    private lateinit var notificationHelper: NotificationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize helper once
        notificationHelper = NotificationHelper(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.my_tasks)

        val btnEn = findViewById<ImageButton>(R.id.btnEn)
        val btnFr = findViewById<ImageButton>(R.id.btnFr)

        btnEn.setOnClickListener {
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("en")
            AppCompatDelegate.setApplicationLocales(appLocale)
        }

        btnFr.setOnClickListener {
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("fr")
            AppCompatDelegate.setApplicationLocales(appLocale)
            viewModel.loadTasks()
        }

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

        viewModel.holidayNames.observe(this) { map ->
            dayAdapter.setHolidayNames(map)
        }

        val years = days.map { it.date.year }.toSet()
        viewModel.loadHolidaysForYears(years, countryCode = "FR")

        // Handle result from AddTaskActivity
        addTaskLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data = result.data ?: return@registerForActivityResult
                    val title = data.getStringExtra("title") ?: return@registerForActivityResult
                    val description = data.getStringExtra("description") ?: ""
                    val dateStr = data.getStringExtra("date") ?: LocalDate.now().toString()
                    val timeStr = data.getStringExtra("time") ?: LocalTime.now().toString()
                    val done = data.getBooleanExtra("done", false)
                    val id = data.getIntExtra("id", -1)

                    if (id != -1) {
                        // Existing task edited
                        viewModel.editTask(id, title, description, dateStr, timeStr, done)
                    } else {
                        // New task created
                        val newId = viewModel.addTask(title, description, dateStr, timeStr, done)

                        // Schedule reminder 5 minutes before task time
                        val date = LocalDate.parse(dateStr)
                        val time = LocalTime.parse(timeStr)
                        notificationHelper.scheduleTaskReminder5MinBefore(
                            title = title,
                            description = description,
                            date = date,
                            time = time
                        )
                    }
                }
            }

        val fab = findViewById<FloatingActionButton>(R.id.fabAddTask)
        fab.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            addTaskLauncher.launch(intent)
        }

        val holidayContainer = findViewById<View>(R.id.holidayContainer)
        val holidayText = findViewById<TextView>(R.id.holidayText)

        viewModel.selectedHolidayName.observe(this) { name ->
            if (!name.isNullOrBlank()) {
                holidayText.text = name
                holidayContainer.visibility = View.VISIBLE
            } else {
                holidayContainer.visibility = View.GONE
            }
        }

        viewModel.openCalendarEvent.observe(this) { task ->
            openCalendarWithTask(task)
        }
    }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_readme -> {
                startActivity(Intent(this, ReadmeActivity::class.java))
                true
            }
            R.id.action_home -> {
                startActivity(Intent(this, LandingActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun openCalendarWithTask(task: Task) {
        val intent: Intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, task.title)
            putExtra(CalendarContract.Events.DESCRIPTION, task.description)

            val startMillis = toMillis(task.date, task.time)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)

            val endMillis = toMillis(task.date, task.time.plusMinutes(15))
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    fun toMillis(date: LocalDate, time: LocalTime): Long {
        val dateTime = LocalDateTime.of(date.year, date.month, date.dayOfMonth, time.hour, time.minute)
        return dateTime
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }
}