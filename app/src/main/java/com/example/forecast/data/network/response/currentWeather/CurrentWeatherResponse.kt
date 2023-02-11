package com.example.forecast.data.network.response.currentWeather


import androidx.room.Embedded
import com.example.forecast.data.db.Entity.currentWeather.CurrentWeatherEntry
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    val latitude: Double,
    val longitude: Double,
//    val resolvedAddress: String,
    val address: String,
    val timezone: String,
    val tzoffset: Double,
    @SerializedName("currentConditions")
    val currentWeatherEntry: CurrentWeatherEntry
)