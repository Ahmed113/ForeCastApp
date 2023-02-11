package com.example.forecast.UI.Base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecast.data.Repo.WeatherRepository
import com.example.forecast.data.db.Entity.WeatherLocation
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

abstract class WeatherViewModel(
    private val weatherRepository: WeatherRepository
):ViewModel() {
    fun isMetric(): Boolean {
        return weatherRepository.isMetric()
    }

    fun getWeatherLocation(): Deferred<LiveData<out WeatherLocation>> {
        return viewModelScope.async {
            weatherRepository.getWeatherLocation()
        }
    }

    fun getDeviceLocationName(deviceLocationLat: Double, deviceLocationLon: Double): String{
        return weatherRepository.getDeviceLocationName(deviceLocationLat,deviceLocationLon)
    }
}