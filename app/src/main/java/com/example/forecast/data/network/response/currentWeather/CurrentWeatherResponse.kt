package com.example.forecast.data.network.response.currentWeather


import com.example.forecast.data.db.entity.currentWeather.CurrentWeatherEntry
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