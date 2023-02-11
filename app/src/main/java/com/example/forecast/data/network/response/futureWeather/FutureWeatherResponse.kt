package com.example.forecast.data.network.response.futureWeather

import androidx.room.Embedded
import com.example.forecast.data.db.Entity.futureWeather.FutureWeatherEntry
import com.google.gson.annotations.SerializedName


data class FutureWeatherResponse(
//    val queryCost: Int,
    val latitude: Double,
    val longitude: Double,
//    val resolvedAddress: String,
    val address: String,
    val timezone: String,
    val tzoffset: Long,
    @SerializedName("days")
    val entries: List<FutureWeatherEntry>
)