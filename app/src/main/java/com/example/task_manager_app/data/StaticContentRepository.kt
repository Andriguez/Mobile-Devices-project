package com.example.task_manager_app.data

import android.content.Context
import com.example.task_manager_app.R

class StaticContentRepository(private val context: Context) {

    /**
     * Returns the localized Readme Title.
     * Android automatically selects the version from values/strings.xml
     * or values-fr/strings.xml based on the app's current locale.
     */
    fun getReadmeTitle(): String {
        return context.getString(R.string.action_readme)
    }

    /**
     * Returns the localized Readme Content.
     * By using context.getString, we ensure that if the user just clicked
     * the French flag, this returns the French text.
     */
    fun getReadmeContent(): String {
        return context.getString(R.string.readme_content)
    }
}