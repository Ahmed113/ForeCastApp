package com.example.forecast.UI.Weather.Future.Details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecast.data.Repo.WeatherRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import org.threeten.bp.LocalDate

class FutureWeatherDetailsVMFactory(
    private val weatherRepository: WeatherRepository,
    private val date: LocalDate
) : ViewModelProvider.NewInstanceFactory() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FutureWeatherDetailsViewModel(weatherRepository, date) as T
    }
}