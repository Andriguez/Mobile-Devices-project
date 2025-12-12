package com.example.task_manager_app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.LocalTime

class TaskListFragment : Fragment(R.layout.fragment_task_list) {

    private lateinit var adapter: TaskAdapter
    private lateinit var allTasks: List<Task>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerTasks)

        allTasks = listOf(
            Task(
                1,
                "Acheter du lait",
                "Supermarché, rayon lait",
                LocalDate.now(),
                LocalTime.of(9, 30)
            ),
            Task(
                2,
                "Appeler Alice",
                "Rappeler au sujet du projet",
                LocalDate.now(),
                LocalTime.of(14, 0)
            ),
            Task(
                3,
                "Envoyer rapport",
                "PDF à joindre",
                LocalDate.now().plusDays(1),
                LocalTime.of(11, 0)
            )
        )

        // Create adapter ONCE
        adapter = TaskAdapter(
            allTasks,
            onTaskClick = { task ->
            },
            onTaskChecked = { task, isChecked ->
            }
        )

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        showTasksForDate(LocalDate.now())
    }

    fun showTasksForDate(date: LocalDate) {
        val filtered = allTasks
            .filter { it.date == date }
            .sortedBy { it.time }

        adapter.updateTasks(filtered)
    }
}
