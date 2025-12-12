package com.example.task_manager_app.ui.landing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task_manager_app.R
import com.example.task_manager_app.model.DayItem
import java.time.LocalDate

class DayAdapter(
    private val days: List<DayItem>,
    private val onDaySelected: (LocalDate) -> Unit
) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    private var selectedDate: LocalDate? = null

    fun setSelectedDate(date: LocalDate) {
        selectedDate = date
        notifyDataSetChanged()
    }

    inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root: View = view.findViewById(R.id.dayRoot)
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

        val isSelected = localDate == selectedDate
        holder.root.setBackgroundResource(
            if (isSelected) R.drawable.day_bg_selected
            else R.drawable.day_bg_normal
        )

        holder.itemView.setOnClickListener {
            setSelectedDate(localDate)
            onDaySelected(localDate)
        }
    }

    override fun getItemCount() = days.size
}
