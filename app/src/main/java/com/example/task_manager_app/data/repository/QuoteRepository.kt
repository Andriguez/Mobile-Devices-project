package com.example.task_manager_app.data.repository

import com.example.task_manager_app.data.remote.QuoteApi
import com.example.task_manager_app.data.remote.QuoteResponse


/**
 * Repository for fetching motivational quotes from external API.
 */
class QuoteRepository(private val api: QuoteApi) {

    suspend fun getRandomQuote(): Result<QuoteResponse> {
        return try {
            val quote = api.getRandomQuote()
            Result.success(quote)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}