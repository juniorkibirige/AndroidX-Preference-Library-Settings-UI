package lan.tmsystem.globochat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.*


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val dataStore = DataStore()
        // enabling the preferenceDatastore disables the sharedPreferences
        // And doesn't automatically span out to children
//        preferenceManager.preferenceDataStore = dataStore


        val accSettingsPref = findPreference<Preference>(getString(R.string.key_account_settings))

        accSettingsPref?.setOnPreferenceClickListener {

            val navHostFragment =
                activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_frag) as NavHostFragment
            val navController = navHostFragment.navController
            val action = SettingsFragmentDirections.actionSettingsToAccSettings()
            navController.navigate(action)

            true
        }

        // Read Preference Values in a fragment
        // Step 1: Get reference to the SharedPreferences (XML File)
        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)

        // Step 2: Get the 'value' using the 'key'
        val autoReplyTime = sharedPreference.getString(getString(R.string.key_auto_reply_time), "")
        Log.i("SettingsFragment", "Auto Reply Time: $autoReplyTime")

        val autoDownload = sharedPreference.getBoolean(getString(R.string.key_auto_download), false)
        Log.i("SettingsFragment", "Auto Download: $autoDownload")

        val statusPref = findPreference<EditTextPreference>(getString(R.string.key_status))
        statusPref?.setOnPreferenceChangeListener { _, newValue ->

            val newStatus = newValue as String
            if (newStatus.contains("bad")) {
                Toast.makeText(
                    context,
                    "Inappropriate Status. Please maintain community guidelines",
                    Toast.LENGTH_SHORT
                ).show()

                false
            } else {
                Log.i("SettingsFragment", "New Status: $newValue")

                true
            }
        }

        val notificationPref = findPreference<SwitchPreferenceCompat>(getString(R.string.key_new_msg_notif))
        notificationPref?.summaryProvider = Preference.SummaryProvider<SwitchPreferenceCompat> { switchPref ->
            if(switchPref?.isChecked!!)
                "Status: ON"
            else
                "Status: OFF"
        }
        notificationPref?.preferenceDataStore = dataStore

        val notifPrefValue = dataStore.getBoolean(getString(R.string.key_new_msg_notif), false)

    }

    class DataStore : PreferenceDataStore() {
        // Override methods only as per need
        // DO NOT override methods which you don't need to use
        // After overriding, remove the super call
        override fun putBoolean(key: String?, value: Boolean) {

            if(key == "key_new_msg_notif") {
                // Save value to cloud or else where
                Log.i("DataStore", "putBoolean executed for $key with new value: $value")
            }
        }

        override fun getBoolean(key: String?, defValue: Boolean): Boolean {

            if(key == "key_new_msg_notif") {
                // Retrieve value from cloud or else where
                Log.i("Datastore", "getBoolean executed for $key")
            }
            return defValue
        }
    }

}