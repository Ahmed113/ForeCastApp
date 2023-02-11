package com.example.forecast.UI.Settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import com.example.forecast.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Sittings"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
    }
}