package com.example.task_manager_app.ui.readme

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.task_manager_app.R
import com.example.task_manager_app.data.StaticContentRepository
import com.example.task_manager_app.viewmodel.ReadmeViewModel
import com.example.task_manager_app.viewmodel.ReadmeViewModelFactory

class ReadmeActivity : AppCompatActivity() {

    private lateinit var viewModel: ReadmeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_readme)

        val textReadmeContent: TextView = findViewById(R.id.textReadmeContent)

        // MVVM Setup: Instantiate Repository and ViewModel
        val repository = StaticContentRepository(applicationContext)
        val factory = ReadmeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ReadmeViewModel::class.java]

        // Observe LiveData for the content and title
        viewModel.readmeText.observe(this) { content ->
            textReadmeContent.text = content
        }

        viewModel.readmeTitle.observe(this) { title ->
            supportActionBar?.title = title
        }

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}