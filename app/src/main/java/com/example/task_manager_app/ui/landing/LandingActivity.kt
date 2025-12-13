package com.example.task_manager_app.ui.landing

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task_manager_app.R
import com.example.task_manager_app.model.DayItem
import com.example.task_manager_app.viewmodel.TaskViewModel
import java.time.LocalDate
import com.example.task_manager_app.utils.generateDayItems


class LandingActivity : AppCompatActivity() {

    private val viewModel: TaskViewModel by viewModels()

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
        val adapter = DayAdapter(days, onDaySelected = { date -> viewModel.selectDate(date) })

        val dayAdapter = DayAdapter(days) { selectedDate ->
            viewModel.selectDate(selectedDate)
        }

        dayAdapter.setSelectedDate(LocalDate.now())

        daysRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        daysRecycler.adapter = dayAdapter

        daysRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        daysRecycler.adapter = dayAdapter

        viewModel.holidays.observe(this) { set ->
            dayAdapter.setHolidays(set)
        }
        viewModel.loadHolidays(year = LocalDate.now().year, countryCode = "FR")

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TaskListFragment())
                .commit()
        }
    }
}
