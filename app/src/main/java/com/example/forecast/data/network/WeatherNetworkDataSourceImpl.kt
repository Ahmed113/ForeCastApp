package com.example.forecast.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.forecast.internal.NoConnectivityException
import com.example.forecast.data.network.response.currentWeather.CurrentWeatherResponse
import com.example.forecast.data.network.response.futureWeather.FutureWeatherResponse

//import com.example.forecast.data.db.Entity.CurrentWeatherResponse

class WeatherNetworkDataSourceImpl(private val weatherAPIService: WeatherAPIService) :
    WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherResponse>()
    //        get() = field
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse> =
        Transformations.map(_downloadedCurrentWeather) { currentWeather -> currentWeather }

    override val downloadedFutureWeather: LiveData<FutureWeatherResponse> =
    Transformations.map(_downloadedFutureWeather) { futureWeather -> futureWeather }


    override suspend fun fetchCurrentWeather(location: String, unit: String) {
        try {
            val fetchedCurrentWeather = weatherAPIService.getCurrntWeather(location, unit)
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
//            Log.d("TAGti","${_downloadedCurrentWeather.value}")
        } catch (e: NoConnectivityException) {
            Log.d("TAG","${downloadedCurrentWeather.value} . ${_downloadedCurrentWeather.value} . No Internet Connection.",e)
        }
    }

    override suspend fun fetchFutureWeather(location: String, unit: String) {
        try {
            val fetchedFutureWeather = weatherAPIService.getFutureWeather(location, unit)
            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        }catch (e: NoConnectivityException) {
            Log.d("TAG","${_downloadedFutureWeather.value} . ${_downloadedFutureWeather.value} . No Internet Connection.",e)
        }
    }
}