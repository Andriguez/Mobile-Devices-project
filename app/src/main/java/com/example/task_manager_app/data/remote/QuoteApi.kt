package com.example.task_manager_app.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Response model for the quote API.
 */
data class QuoteResponse(
    val content: String,
    val author: String
)

/**
 * Retrofit service interface for fetching motivational quotes.
 */
interface QuoteApi {

    @GET("random")
    suspend fun getRandomQuote(): QuoteResponse

    companion object {
        private const val BASE_URL = "https://api.quotable.io/"

        fun create(): QuoteApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(QuoteApi::class.java)
        }
    }
}