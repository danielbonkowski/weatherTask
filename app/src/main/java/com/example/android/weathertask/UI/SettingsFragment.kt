package com.example.android.weathertask.UI

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.preference.*
import com.example.android.weathertask.R
import com.example.android.weathertask.Utils
import com.example.android.weathertask.ViewModel.SharedViewModel
import com.example.android.weathertask.databinding.FragmentSettingsBinding


/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences_screen)

        val sharedPreferences = preferenceScreen.sharedPreferences
        val prefScreen = preferenceScreen

        val count = prefScreen.preferenceCount - 1
        for(i in 0..count){
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
        if(!key.isNullOrEmpty()){
            val preference = findPreference<Preference>(key)
            if(preference != null && preference is ListPreference){
                if (sharedPreferences != null) {
                    val result = sharedPreferences.getString(key, "")?.let { setPreferenceSummary(preference, it) }

                    sharedViewModel.selectUnit(Utils.isMetric(requireContext()))
                }
            }
        }
    }

    private fun setPreferenceSummary(preference: Preference, value: Any){
        val stringValue = value.toString()
        val key = preference.key

        if(preference is ListPreference){
            var listPreference: ListPreference = preference;
            val preferenceIndex = listPreference.findIndexOfValue(stringValue)
            if(preferenceIndex >= 0){
                preference.summary = listPreference.entries[preferenceIndex]
            }
        }else{
            preference.summary = stringValue
        }
    }
}