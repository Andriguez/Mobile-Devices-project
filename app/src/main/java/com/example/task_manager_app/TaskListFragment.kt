package com.example.task_manager_app;

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

class TaskListFragment : Fragment(R.layout.fragment_task_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerTasks)

        val sampleTasks = listOf(
            Task(1, "Acheter du lait", "Supermarché, rayon lait"),
            Task(2, "Appeler Alice", "Rappeler au sujet du projet"),
            Task(3, "Envoyer rapport", "PDF à joindre et envoyer au manager")
        )

        val adapter = TaskAdapter(
            sampleTasks,
            onTaskClick = { task ->
                // handle item click
            },
            onTaskChecked = { task, isChecked ->
                // handle checkbox toggle
            }
        )

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
    }
}
