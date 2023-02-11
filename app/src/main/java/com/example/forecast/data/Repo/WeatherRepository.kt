package com.example.forecast.data.Repo

import androidx.lifecycle.LiveData
import com.example.forecast.data.db.Entity.WeatherLocation
//import com.example.forecast.data.db.Entity.CurrentWeatherEntry
//import com.example.forecast.data.db.Entity.CurrentWeatherResponse
//import com.example.forecast.data.db.Entity.WeatherLocation
import com.example.forecast.data.db.Entity.currentWeather.CurrentWeatherEntry
import com.example.forecast.data.db.specificFutureWeatherEntries.DetailedFutureWeatherEntries
import com.example.forecast.data.db.specificFutureWeatherEntries.SimpleFutureWeatherEntries
import org.threeten.bp.LocalDate

//import com.example.forecast.data.db.Entity.currentWeather.WeatherLocation

interface WeatherRepository {
    suspend fun getCurrentWeather(): LiveData<out CurrentWeatherEntry>
    suspend fun getFutureWeatherList(startDate: LocalDate): LiveData<out List<SimpleFutureWeatherEntries>>
    suspend fun getDetailedFutureWeatherByDate(date : LocalDate) : LiveData<out DetailedFutureWeatherEntries>
    suspend fun getWeatherLocation(): LiveData<out WeatherLocation>

    fun isMetric(): Boolean
    fun getDeviceLocationName(deviceLocationLat: Double, deviceLocationLon: Double): String
}
