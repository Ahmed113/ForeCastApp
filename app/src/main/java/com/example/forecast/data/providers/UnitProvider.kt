package com.example.forecast.data.providers

import com.example.forecast.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
    fun onUnitChange(unitSystem: UnitSystem): Boolean
}