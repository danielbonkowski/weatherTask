package com.example.android.weathertask.UI

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.preference.*
import com.example.android.weathertask.R
import com.example.android.weathertask.Utils.Utils
import com.example.android.weathertask.ViewModel.SharedViewModel


class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences_screen)

        val sharedPreferences = preferenceScreen.sharedPreferences
        val prefScreen = preferenceScreen

        setupPreferences(prefScreen, sharedPreferences)
    }

    private fun setupPreferences(
        prefScreen: PreferenceScreen,
        sharedPreferences: SharedPreferences
    ) {
        val count = prefScreen.preferenceCount - 1
        for (i in 0..count) {
            val pref = prefScreen.getPreference(i)
            val value = sharedPreferences.getString(pref.key, "")
            if (value != null) {
                setPreferenceSummary(pref, value)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (!key.isNullOrEmpty()) {
            val preference = findPreference<Preference>(key)
            if (preference != null && preference is ListPreference) {
                if (sharedPreferences != null) {
                    sharedPreferences.getString(key, "")
                        ?.let { setPreferenceSummary(preference, it) }
                    sharedViewModel.selectUnit(Utils.isMetric(requireContext()))
                }
            }
        }
    }

    private fun setPreferenceSummary(preference: Preference, value: Any) {
        val stringValue = value.toString()

        if (preference is ListPreference) {
            val listPreference: ListPreference = preference
            val preferenceIndex = listPreference.findIndexOfValue(stringValue)
            if (preferenceIndex >= 0) {
                preference.summary = listPreference.entries[preferenceIndex]
            }
        } else {
            preference.summary = stringValue
        }
    }
}