package com.example.task_manager_app.ui.landing

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task_manager_app.R
import com.example.task_manager_app.viewmodel.TaskViewModel
import java.time.LocalDate

class TaskListFragment : Fragment(R.layout.fragment_task_list) {

    private lateinit var adapter: TaskAdapter
    private val viewModel: TaskViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerTasks)

        adapter = TaskAdapter(
            emptyList(),
            onTaskClick = { /* handle click */ },
            onTaskChecked = { _, _ -> }
        )

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            adapter.updateTasks(tasks)
        }

        if (savedInstanceState == null) {
            viewModel.loadTasks()
            viewModel.selectDate(LocalDate.now())
        }
    }
}
