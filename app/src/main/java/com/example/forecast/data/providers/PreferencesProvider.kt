package com.example.forecast.data.providers

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

abstract class PreferencesProvider(context : Context) {
    private val appContext = context.applicationContext
    protected val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)
}