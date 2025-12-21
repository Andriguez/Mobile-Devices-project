package com.example.task_manager_app.ui.taskcreation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.task_manager_app.R
import java.time.LocalDate
import java.time.LocalTime

class AddTaskActivity : AppCompatActivity() {

    private var selectedDate: LocalDate = LocalDate.now()
    private var selectedTime: LocalTime = LocalTime.now().withSecond(0).withNano(0)
    private var editingId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val inputTitle = findViewById<EditText>(R.id.inputTitle)
        val inputDescription = findViewById<EditText>(R.id.inputDescription)
        val buttonDate = findViewById<Button>(R.id.buttonDate)
        val buttonTime = findViewById<Button>(R.id.buttonTime)
        val checkboxDone = findViewById<CheckBox>(R.id.checkboxDone)
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        val buttonCancel = findViewById<Button>(R.id.buttonCancel)

        // Si on reçoit une tâche à éditer, préremplir
        editingId = intent.getIntExtra("id", -1).takeIf { it != -1 }
        intent.getStringExtra("title")?.let { inputTitle.setText(it) }
        intent.getStringExtra("description")?.let { inputDescription.setText(it) }
        intent.getStringExtra("date")?.let {
            try {
                selectedDate = LocalDate.parse(it)
            } catch (_: Exception) {
            }
        }
        intent.getStringExtra("time")?.let {
            try {
                selectedTime = LocalTime.parse(it)
            } catch (_: Exception) {
            }
        }
        intent.getBooleanExtra("done", false).let { checkboxDone.isChecked = it }

        fun updateDateButton() {
            buttonDate.text = selectedDate.toString()
        }

        fun updateTimeButton() {
            buttonTime.text = selectedTime.toString()
        }

        updateDateButton(); updateTimeButton()

        buttonDate.setOnClickListener {
            val dp = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    updateDateButton()
                },
                selectedDate.year,
                selectedDate.monthValue - 1,
                selectedDate.dayOfMonth
            )
            dp.show()
        }

        buttonTime.setOnClickListener {
            val tp = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    selectedTime = LocalTime.of(hourOfDay, minute)
                    updateTimeButton()
                },
                selectedTime.hour,
                selectedTime.minute,
                true
            )
            tp.show()
        }

        buttonSave.setOnClickListener {
            val title = inputTitle.text.toString().trim()
            if (title.isEmpty()) {
                Toast.makeText(this, getString(R.string.enter_title), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val description = inputDescription.text.toString().trim()
            val done = checkboxDone.isChecked

            val out = Intent().apply {
                putExtra("title", title)
                putExtra("description", description)
                putExtra("date", selectedDate.toString())
                putExtra("time", selectedTime.toString())
                putExtra("done", done)
                editingId?.let { putExtra("id", it) }
            }
            setResult(RESULT_OK, out)
            finish()
        }

        buttonCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}