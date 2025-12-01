package com.example.task_manager_app.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task_manager_app.data.remote.QuoteApi
import com.example.task_manager_app.data.remote.QuoteResponse
import com.example.task_manager_app.data.repository.QuoteRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for managing motivational quotes from external API.
 */
class QuoteViewModel : ViewModel() {

    private val repository = QuoteRepository(QuoteApi.create())

    private val _quote = MutableLiveData<QuoteResponse?>()
    val quote: LiveData<QuoteResponse?> = _quote

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchRandomQuote() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.getRandomQuote()

            result.onSuccess { quoteResponse ->
                _quote.value = quoteResponse
            }.onFailure { exception ->
                _error.value = exception.message ?: "Failed to fetch quote"
            }

            _isLoading.value = false
        }
    }

}