package com.example.forecast.ui.weather.future.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.forecast.forecastViewModel.WeatherViewModel
import com.example.forecast.data.repository.WeatherRepository
import com.example.forecast.data.db.specificFutureWeatherEntries.SimpleFutureWeatherEntries
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import org.threeten.bp.LocalDate

class FutureWeatherViewModel(
    private val weatherRepository: WeatherRepository
) : WeatherViewModel(weatherRepository) {

    fun getFutureWeather(): Deferred<LiveData<out List<SimpleFutureWeatherEntries>>> {
        return viewModelScope.async {
            weatherRepository.getFutureWeatherList(LocalDate.now())
        }
    }
}