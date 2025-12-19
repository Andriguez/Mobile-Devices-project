// kotlin
package com.example.task_manager_app.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate

class HolidayRepository {

    suspend fun getPublicHolidays(year: Int, countryCode: String): Set<LocalDate> =
        withContext(Dispatchers.IO) {
            var conn: HttpURLConnection? = null
            try {
                val url = URL("https://date.nager.at/api/v3/PublicHolidays/$year/$countryCode")
                conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.connectTimeout = 5000
                conn.readTimeout = 5000

                if (conn.responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e("HolidayRepository", "HTTP error code: ${conn.responseCode}")
                    return@withContext emptySet()
                }

                val text = conn.inputStream.bufferedReader().use { it.readText() }
                val arr = JSONArray(text)
                val set = mutableSetOf<LocalDate>()
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    val dateStr = obj.getString("date") // format "YYYY-MM-DD"
                    set.add(LocalDate.parse(dateStr))
                }
                set
            } catch (e: Exception) {
                Log.e("HolidayRepository", "Failed to load holidays", e)
                emptySet()
            } finally {
                try {
                    conn?.disconnect()
                } catch (_: Exception) { /* ignore */
                }
            }
        }

    // retourne date -> nom en anglais si possible (prefère "name" puis "localName")
    suspend fun getPublicHolidaysWithNames(year: Int, countryCode: String): Map<LocalDate, String> =
        withContext(Dispatchers.IO) {
            var conn: HttpURLConnection? = null
            try {
                val url = URL("https://date.nager.at/api/v3/PublicHolidays/$year/$countryCode")
                conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.connectTimeout = 5000
                conn.readTimeout = 5000

                if (conn.responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e("HolidayRepository", "HTTP error code: ${conn.responseCode}")
                    return@withContext emptyMap()
                }

                val text = conn.inputStream.bufferedReader().use { it.readText() }
                val arr = JSONArray(text)
                val map = mutableMapOf<LocalDate, String>()
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    val dateStr = obj.getString("date")
                    // préférer "name" (généralement anglais), fallback sur "localName"
                    val name = when {
                        obj.has("name") -> obj.optString("name", "")
                        obj.has("localName") -> obj.optString("localName", "")
                        else -> ""
                    }
                    map[LocalDate.parse(dateStr)] = name
                }
                map
            } catch (e: Exception) {
                Log.e("HolidayRepository", "Failed to load holidays with names", e)
                emptyMap()
            } finally {
                try {
                    conn?.disconnect()
                } catch (_: Exception) { /* ignore */
                }
            }
        }
}
