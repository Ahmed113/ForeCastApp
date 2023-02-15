package com.example.forecast.ui.weather.future.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.forecast.forecastViewModel.WeatherViewModel
import com.example.forecast.data.repository.WeatherRepository
import com.example.forecast.data.db.specificFutureWeatherEntries.DetailedFutureWeatherEntries
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import org.threeten.bp.LocalDate

class FutureWeatherDetailsViewModel(
    private val weatherRepository: WeatherRepository,
    private val date: LocalDate
) : WeatherViewModel(weatherRepository) {

    fun getDetailsOfFutureWeatherByDate(): Deferred<LiveData<out DetailedFutureWeatherEntries>> {
        return viewModelScope.async {
            weatherRepository.getDetailedFutureWeatherByDate(date)
        }
    }
}