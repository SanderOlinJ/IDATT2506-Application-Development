package edu.ntnu.idatt2506.assignment_07.managers

import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import edu.ntnu.idatt2506.assignment_07.R

/**
 * Manager for application preferences related operations.
 *
 * @property activity The activity in which the preference manager operates.
 */
class AppPreferenceManager(private val activity: AppCompatActivity) {

    // The resources associated with the activity
    private val resources = activity.resources

    // The default shared preferences associated with the activity
    private val preferences = PreferenceManager.getDefaultSharedPreferences(activity)


    /**
     * Gets the stored string preference.
     *
     * @param key The key to retrieve its associated value.
     * @param defaultValue The default value to return if the key isn't found.
     * @return The stored preference string or the default value if the key isn't found.
     */
    private fun getString(key: String, defaultValue: String): String {
        return preferences.getString(key, defaultValue) ?: defaultValue
    }


    /**
     * Updates the background color of the activity based on the saved preference.
     */
    fun updateBackgroundColor() {
        // Get the array of background color values from resources
        val backgroundColorValues = resources.getStringArray(R.array.background_color_values)

        // Get the saved background color preference or default to white
        val value = getString(
            resources.getString(R.string.background_color),
            resources.getString(R.string.background_color_default)
        )

        // Set the background color based on the saved preference
        when (value) {
            backgroundColorValues[0] -> activity.window.decorView.setBackgroundColor(Color.WHITE)
            backgroundColorValues[1] -> activity.window.decorView
                .setBackgroundColor(Color.parseColor("#ADD8E6"))
            backgroundColorValues[2] -> activity.window.decorView
                .setBackgroundColor(Color.parseColor("#90EE90"))
        }
    }

    /**
     * Registers a shared preferences change listener.
     *
     * @param activity The listener to register.
     */
    fun registerListener(activity: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(activity)
    }

    /**
     * Unregisters a shared preferences change listener.
     *
     * @param activity The listener to unregister.
     */
    fun unregisterListener(activity: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.unregisterOnSharedPreferenceChangeListener(activity)
    }
}