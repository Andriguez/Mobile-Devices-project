package com.example.task_manager_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.title = getString(R.string.mes_t_ches)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TaskListFragment())
                .commit()
        }
    }
}
