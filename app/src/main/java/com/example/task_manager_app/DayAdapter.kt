package com.example.task_manager_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate

class DayAdapter(
    private val days: List<DayItem>,
    private val onDaySelected: (LocalDate) -> Unit
) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val day: TextView = view.findViewById(R.id.textDay)
        val date: TextView = view.findViewById(R.id.textDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val dayItem = days[position]
        val localDate = dayItem.date

        holder.day.text = localDate.dayOfWeek.name.take(3)
        holder.date.text = localDate.dayOfMonth.toString()

        holder.itemView.setOnClickListener {
            onDaySelected(localDate)
        }
    }

    override fun getItemCount() = days.size
}
