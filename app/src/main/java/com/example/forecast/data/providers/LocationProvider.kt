package com.example.forecast.data.providers

import com.example.forecast.data.db.Entity.WeatherLocation

interface LocationProvider {
    suspend fun hasLocationChanged(lastLocation : WeatherLocation): Boolean
    suspend fun getPreferredLocation(): String
    fun getDeviceLocationName(deviceLocationLat: Double, deviceLocationLon: Double): String
}