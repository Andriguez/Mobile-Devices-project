package com.example.taskmanager

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

        val adapter = TaskAdapter(sampleTasks) { task ->
            // action au clic (placeholder)
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }
}
