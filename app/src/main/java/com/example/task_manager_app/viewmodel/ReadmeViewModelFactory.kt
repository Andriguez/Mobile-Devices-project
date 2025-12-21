package com.example.task_manager_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.task_manager_app.data.StaticContentRepository

class ReadmeViewModelFactory(private val repository: StaticContentRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReadmeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReadmeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}