package com.example.forecast.data.providers

import android.content.Context
import com.example.forecast.internal.UnitSystem

const val UNIT_SYSTEM = "UNIT_SYSTEM"

class UnitProviderImpl(context: Context) : PreferencesProvider(context), UnitProvider {

    override fun getUnitSystem(): UnitSystem {
        val uniteName = preferences.getString(UNIT_SYSTEM, UnitSystem.METRIC.name)
        return UnitSystem.valueOf(uniteName!!)
    }

    private var mCurrentUnit = getUnitSystem().name

    override fun onUnitChange(unitSystem: UnitSystem): Boolean {
        return if (unitSystem.name == mCurrentUnit) {
            false
        } else {
            mCurrentUnit = unitSystem.name
            true
        }
    }
}

