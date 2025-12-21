package com.example.task_manager_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.task_manager_app.data.StaticContentRepository

class ReadmeViewModel(private val repository: StaticContentRepository) : ViewModel() {

    private val _readmeText = MutableLiveData<String>()
    val readmeText: LiveData<String> = _readmeText

    private val _readmeTitle = MutableLiveData<String>()
    val readmeTitle: LiveData<String> = _readmeTitle

    init {
        // Load the data immediately upon ViewModel creation
        _readmeText.value = repository.getReadmeContent()
        _readmeTitle.value = repository.getReadmeTitle()
    }
}