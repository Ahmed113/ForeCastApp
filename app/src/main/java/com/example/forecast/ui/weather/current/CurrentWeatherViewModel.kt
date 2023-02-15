package com.example.forecast.ui.weather.current

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.forecast.forecastViewModel.WeatherViewModel
import com.example.forecast.data.repository.WeatherRepository
import com.example.forecast.data.db.entity.currentWeather.CurrentWeatherEntry
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class CurrentWeatherViewModel(private val weatherRepository: WeatherRepository) :
    WeatherViewModel(weatherRepository) {

    fun getCurrentWeather(): Deferred<LiveData<out CurrentWeatherEntry>> {
        return viewModelScope.async {
            weatherRepository.getCurrentWeather()
        }
    }
}

//    private val _currentWeather = MutableLiveData<CurrentWeatherResponse>()
//
//    //        get() = field
//    val currentWeather: LiveData<CurrentWeatherResponse> =
//        Transformations.map(_currentWeather) { currentWeather -> currentWeather }

//    init {
//        currentWeatherRepositoryImpl = CurrentWeatherRepositoryImpl(application,weatherNetworkDataSource)
////        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeatherResponse ->
////            persistCurrentWeather(newCurrentWeatherResponse)
////            Log.d("tagr", "new:$newCurrentWeatherResponse ")
////        }
//    }

//    private fun persistCurrentWeather(newCurrentWeatherResponse: CurrentWeatherResponse) {
//        viewModelScope.launch(Dispatchers.IO) {
//            currentWeatherRepositoryImpl.upsert(newCurrentWeatherResponse)
//        }
//    }

//////////////////latest updates/////////////////////

//    private val unit:UnitSystem = unitProvider.getUnitSystem()
//
//    fun isMetric():Boolean{
//      return unit == UnitSystem.METRIC
//    }

//private val unit:UnitSystem
//    get() = unitProvider.getUnitSystem()
//
//private val isUnitChanged : Boolean
//    get() = unitProvider.onUnitChange(unit)

//    @RequiresApi(Build.VERSION_CODES.O)
////    @DelicateCoroutinesApi
//    suspend fun getcurrentWeather(): Deferred<LiveData<out CurrentWeatherResponse>> {
//        currentWeatherRepo.updateCurrentWeather().await()
//       return viewModelScope.async {
//            currentWeatherRepo.getCurrentWeather()
////        Log.d("TAGl", ":${currentWeatherRepo.getCurrentWeather().value}")
//        }
//    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private suspend fun updateCurrentWeather(): Deferred<Unit> {
//        return viewModelScope.async {
////            val weatherLocation = WeatherLocationDAO.getWeatherLocation().value
//            if (isFetchCurrentWeather(ZonedDateTime.now().minusHours(1))||isUnitChanged){
//                fetchCurrentWeather("London",unit)
//                Log.d("unitd", "updateCurrentWeather: ${unit.name/*getUnitSystem().*/}")
//                Log.d("unitd", "updateCurrentWeather: $isUnitChanged")
//            }
////            Log.d("TAGr2","updateCurrentWeather:${weatherNetworkDataSource.downloadedCurrentWeather.value} ")
//        }
//    }
//
//    private suspend fun fetchCurrentWeather(location:String, unit:UnitSystem){
//        weatherNetworkDataSource.fetchCurrentWeather(location, unit.name)
//    }
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun isFetchCurrentWeather(lastFetchTime: ZonedDateTime): Boolean {
//        val twentyMinutesAgo = ZonedDateTime.now().minusMinutes(20)
//        return lastFetchTime.isBefore(twentyMinutesAgo)
//    }