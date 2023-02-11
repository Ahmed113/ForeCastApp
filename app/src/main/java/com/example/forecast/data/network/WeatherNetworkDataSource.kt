package com.example.forecast.data.network

import androidx.lifecycle.LiveData
import com.example.forecast.data.db.Entity.futureWeather.FutureWeatherEntry
import com.example.forecast.data.network.response.currentWeather.CurrentWeatherResponse
import com.example.forecast.data.network.response.futureWeather.FutureWeatherResponse

//import com.example.forecast.data.db.Entity.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
    val downloadedFutureWeather: LiveData<FutureWeatherResponse>

    suspend fun fetchCurrentWeather(location:String, unit: String)
    suspend fun fetchFutureWeather(location:String, unit: String)
}