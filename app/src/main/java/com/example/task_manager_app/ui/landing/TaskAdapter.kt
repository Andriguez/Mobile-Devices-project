package com.example.task_manager_app.ui.landing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task_manager_app.R
import com.example.task_manager_app.model.Task
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class TaskAdapter(
    private var tasks: List<Task>,
    private val onTaskClick: (Task) -> Unit,
    private val onTaskChecked: (Task, Boolean) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.task_title)
        val description: TextView = itemView.findViewById(R.id.task_description)
        val checkBox: CheckBox = itemView.findViewById(R.id.task_checkbox)
        val time: TextView = itemView.findViewById(R.id.task_time)
        val delayText: TextView = itemView.findViewById(R.id.delayText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.title.text = task.title
        holder.description.text = task.description

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = task.done
        holder.time.text = task.time.toString()


        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            task.done = isChecked
            onTaskChecked(task, isChecked)
        }

        holder.itemView.setOnClickListener {
            onTaskClick(task)
        }

        val nowDate = LocalDate.now()
        val nowTime = LocalTime.now()

        val isOverdue =
            task.date == nowDate &&
                    task.time.isBefore(nowTime) &&
                    !task.done

        holder.itemView.setBackgroundResource(
            if (isOverdue) R.drawable.task_bg_overdue
            else R.drawable.task_bg_normal
        )

        if (isOverdue) {
            val delayMinutes = ChronoUnit.MINUTES.between(task.time, nowTime)

            val delayText = if (delayMinutes < 60) {
                "Delayed by ${delayMinutes} min"
            } else {
                val h = delayMinutes / 60
                val m = delayMinutes % 60
                "Delayed by ${h}h ${m}m"
            }

            holder.delayText.text = delayText
            holder.delayText.visibility = View.VISIBLE
        } else {
            holder.delayText.visibility = View.GONE
        }


    }

    override fun getItemCount(): Int = tasks.size
}
