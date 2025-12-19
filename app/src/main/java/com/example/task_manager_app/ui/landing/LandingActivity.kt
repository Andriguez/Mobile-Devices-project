package com.example.task_manager_app.ui.landing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.example.task_manager_app.R
import com.example.task_manager_app.ui.taskcreation.AddTaskActivity
import com.example.task_manager_app.ui.main.MainActivity
import com.example.task_manager_app.ui.readme.ReadmeActivity
import com.example.task_manager_app.viewmodel.LandingActivityViewModel


class LandingActivity : AppCompatActivity() {
    private val viewModel: LandingActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val viewTasksBtn = findViewById<Button>(R.id.view_tasks_btn)
        viewTasksBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val addTaskBtn = findViewById<Button>(R.id.add_task_btn)

        addTaskBtn.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        val readmeBtn = findViewById<Button>(R.id.readme_btn)
        readmeBtn.setOnClickListener {
            startActivity(Intent(this, ReadmeActivity::class.java))
        }

    }
}