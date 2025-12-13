package com.example.task_manager_app.ui.landing

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.task_manager_app.R
import com.example.task_manager_app.model.DayItem
import java.time.LocalDate
import com.example.task_manager_app.utils.toShortMonth
import java.util.Locale
import com.example.task_manager_app.utils.generateDayItems
import androidx.core.graphics.toColorInt

class DayAdapter(
    private val days: List<DayItem>,
    private val onDaySelected: (LocalDate) -> Unit

) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    private var selectedDate: LocalDate? = null
    private var holidays: Set<LocalDate> = emptySet()


    fun setSelectedDate(date: LocalDate) {
        selectedDate = date
        notifyDataSetChanged()
    }

    fun setHolidays(h: Set<LocalDate>) {
        holidays = h
        notifyDataSetChanged()
    }

    inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root: View = view.findViewById(R.id.dayRoot)
        val day: TextView = view.findViewById(R.id.textDay)
        val date: TextView = view.findViewById(R.id.textDate)
        val todayLabel: TextView = view.findViewById(R.id.textTodayLabel)

        val month: TextView = view.findViewById(R.id.textMonth)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val dayItem = days[position]
        val localDate = dayItem.date
        val today = LocalDate.now()

        if (localDate == today) {
            holder.todayLabel.visibility = View.VISIBLE
        } else {
            holder.todayLabel.visibility = View.GONE
        }

        holder.day.text = localDate.dayOfWeek.name.take(3)
        holder.date.text = localDate.dayOfMonth.toString()
        holder.month.text = localDate.toShortMonth(Locale.ENGLISH)

        val isHoliday = holidays.contains(localDate)
        if (isHoliday) {
            // couleur bleue pour les jours fériés
            val blue = "#1976D2".toColorInt() // material blue 700
            holder.day.setTextColor(blue)
            holder.date.setTextColor(blue)
            holder.month.setTextColor(blue)
        } else {
            val normal = ContextCompat.getColor(holder.itemView.context, R.color.black)
            holder.day.setTextColor(normal)
            holder.date.setTextColor(normal)
            holder.month.setTextColor(normal)

        }

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
