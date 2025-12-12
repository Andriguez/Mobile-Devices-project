package com.example.task_manager_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate

class LandingActivity : AppCompatActivity() {

    private lateinit var taskListFragment: TaskListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.mes_t_ches)

        // Create or recover fragment FIRST
        taskListFragment = if (savedInstanceState == null) {
            TaskListFragment().also {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, it)
                    .commit()
            }
        } else {
            supportFragmentManager.findFragmentById(R.id.fragment_container) as TaskListFragment
        }

        val daysRecycler = findViewById<RecyclerView>(R.id.recyclerDays)

        val days = (0..6).map { DayItem(LocalDate.now().plusDays(it.toLong())) }

        val dayAdapter = DayAdapter(days) { selectedDate ->
            taskListFragment.showTasksForDate(selectedDate)
        }

        daysRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        daysRecycler.adapter = dayAdapter
    }
}
