package edu.ntnu.idatt2506.assignment_07

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import edu.ntnu.idatt2506.assignment_07.databinding.ActivitySettingsBinding
import edu.ntnu.idatt2506.assignment_07.managers.AppPreferenceManager

/**
 * Activity responsible for managing and displaying app settings.
 * Implements `SharedPreferences.OnSharedPreferenceChangeListener` to respond to preference changes.
 * Implements `Preference.SummaryProvider<ListPreference>` to provide summary for certain preferences.
 */
class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener,
    Preference.SummaryProvider<ListPreference> {

    // Binding object for the activity settings layout.
    private lateinit var ui: ActivitySettingsBinding

    // Manager to handle app-specific preferences.
    private lateinit var myPreferenceManager: AppPreferenceManager

    /**
     * Called when the activity is starting.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the preference manager and register this activity as a listener to changes.
        myPreferenceManager = AppPreferenceManager(this)
        myPreferenceManager.registerListener(this)

        // Inflate the activity layout.
        ui = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(ui.root)

        // Begin a new FragmentTransaction in which SettingsFragment is added to the activity.
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, SettingsFragment())
            .commit()

        // Set a click listener to the button which finishes (closes) the current activity.
        ui.button.setOnClickListener {
            finish()
        }
    }

    /**
     * Called after [onRestart] or [onPause].
     * Used here to update the background color.
     */
    override fun onResume() {
        super.onResume()
        AppPreferenceManager(this).updateBackgroundColor()
    }

    /**
     * Called when a shared preference is changed, added, or removed.
     * @param sharedPreferences The SharedPreferences that received the change.
     * @param key The key of the preference that was changed, added, or removed.
     */
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == getString(R.string.background_color)) myPreferenceManager.updateBackgroundColor()
    }

    /**
     * Perform any final cleanup before the activity is destroyed.
     * Used here to unregister this activity as a listener.
     */
    override fun onDestroy() {
        super.onDestroy()
        myPreferenceManager.unregisterListener(this)
    }

    /**
     * Provides a summary for a given preference. This method is specific for ListPreference.
     * @param preference The preference for which the summary should be provided.
     * @return The summary for the given preference.
     */
    override fun provideSummary(preference: ListPreference): CharSequence? {
        return when (preference.key) {
            getString(R.string.background_color) -> preference.entry
            else                           -> "Unknown Preference"
        }
    }

    /**
     * Fragment to display app settings.
     */
    class SettingsFragment : PreferenceFragmentCompat() {

        /**
         * Called during [onCreate] to supply the preferences for this fragment.
         * Preferences will be shown as determined by the preference XML file.
         * @param savedInstanceState If the fragment is being re-created
         * from a previous saved state, this is the state.
         * @param rootKey If non-null, this preference fragment should be rooted at
         * the [PreferenceScreen] with this key.
         */
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preference_screen, rootKey)
        }
    }
}