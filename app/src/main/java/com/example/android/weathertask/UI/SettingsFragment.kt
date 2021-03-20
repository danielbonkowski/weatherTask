package com.example.android.weathertask.UI

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.preference.*
import com.example.android.weathertask.R
import com.example.android.weathertask.databinding.FragmentSettingsBinding


/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences_screen)
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
                val value = sharedPreferences?.getString(key, "")
                val listValueIndex = preference.findIndexOfValue(value)
                if(listValueIndex >= 0){
                    preference.summary = preference.entries[listValueIndex]
                }
            }
        }


    }
}