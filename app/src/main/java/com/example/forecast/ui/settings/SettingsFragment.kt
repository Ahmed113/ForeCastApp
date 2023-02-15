package com.example.forecast.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.example.forecast.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Sittings"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
    }
}