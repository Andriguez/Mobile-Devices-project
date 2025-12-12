package com.example.task_manager_app.ui.landing

import android.os.Bundle
import android.view.View
import android.widget.TextView
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
    private lateinit var activeAdapter: TaskAdapter
    private lateinit var doneAdapter: TaskAdapter
    private var doneExpanded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activeRecycler = view.findViewById<RecyclerView>(R.id.recyclerActiveTasks)
        val doneRecycler = view.findViewById<RecyclerView>(R.id.recyclerDoneTasks)
        val toggleText = view.findViewById<TextView>(R.id.textToggleDone)

        activeAdapter = TaskAdapter(emptyList(), { }, { _, _ -> })
        doneAdapter = TaskAdapter(emptyList(), { }, { _, _ -> })

        activeRecycler.layoutManager = LinearLayoutManager(requireContext())
        doneRecycler.layoutManager = LinearLayoutManager(requireContext())

        activeRecycler.adapter = activeAdapter
        doneRecycler.adapter = doneAdapter

        viewModel.activeTasks.observe(viewLifecycleOwner) {
            activeAdapter.updateTasks(it)
        }

        viewModel.doneTasks.observe(viewLifecycleOwner) {
            doneAdapter.updateTasks(it)
            toggleText.text = if (doneExpanded) {
                getString(R.string.hide_done)
            } else {
                getString(R.string.view_done, it.size)
            }
        }

        if (savedInstanceState == null) {
            viewModel.loadTasks()
            viewModel.selectDate(LocalDate.now())
        }

        updateDoneToggleUi(toggleText)

        toggleText.setOnClickListener {
            doneExpanded = !doneExpanded
            doneRecycler.visibility =
                if (doneExpanded) View.VISIBLE else View.GONE

            updateDoneToggleUi(toggleText)
        }

    }

    private fun updateDoneToggleUi(toggleText: TextView) {
        val arrow = if (doneExpanded) {
            R.drawable.ic_arrow_down
        } else {
            R.drawable.ic_arrow_right
        }

        toggleText.setCompoundDrawablesWithIntrinsicBounds(
            arrow, 0, 0, 0
        )

        toggleText.text = if (doneExpanded) {
            getString(R.string.hide_done)
        } else {
            getString(R.string.view_done, doneAdapter.itemCount)
        }
    }
}
