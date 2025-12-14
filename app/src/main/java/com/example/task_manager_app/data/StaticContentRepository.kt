package com.example.task_manager_app.data

import android.content.Context
import com.example.task_manager_app.R

class StaticContentRepository(private val context: Context) {

    fun getReadmeTitle(): String {
        return context.getString(R.string.readme_title)
    }

    fun getReadmeContent(): String {
        return context.getString(R.string.readme_content)
    }
}