package com.example.task_manager_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val recycler: RecyclerView = findViewById(R.id.recyclerTasks)
        val sampleTasks = listOf(
                Task(1, "Acheter du lait", "Supermarché, rayon lait"),
                Task(2, "Appeler Alice", "Rappeler au sujet du projet"),
                Task(3, "Envoyer rapport", "PDF à joindre et envoyer au manager")
        )

        val adapter = TaskAdapter(
            sampleTasks,
            onTaskClick = { task ->
                // item clicked
            },
            onTaskChecked = { task, isChecked ->
                // checkbox toggled
            }
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }
}
