package com.example.task_manager_app.ui.readme

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
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
        // applicationContext ensures we use the correct resources for the current locale
        val repository = StaticContentRepository(applicationContext)
        val factory = ReadmeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ReadmeViewModel::class.java]

        // Observe LiveData for the content
        viewModel.readmeText.observe(this) { content ->
            if (content != null) {
                // Use HtmlCompat to render <b> tags and handle the CDATA formatting
                textReadmeContent.text = HtmlCompat.fromHtml(
                    content,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
        }

        // Observe LiveData for the title
        viewModel.readmeTitle.observe(this) { title ->
            supportActionBar?.title = title
        }

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Handles the back button click in the toolbar
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}