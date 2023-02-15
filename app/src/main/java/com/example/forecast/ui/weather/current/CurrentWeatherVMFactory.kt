package com.example.forecast.ui.weather.current

//import com.example.forecast.data.Repo.CurrentWeatherRepoImpl
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecast.data.repository.WeatherRepository
import kotlinx.coroutines.DelicateCoroutinesApi

class CurrentWeatherVMFactory(
    private val weatherRepository: WeatherRepository
) : ViewModelProvider.NewInstanceFactory() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(weatherRepository) as T
    }
}