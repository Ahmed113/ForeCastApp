package com.example.forecast.data.repository

//import com.example.forecast.data.db.Entity.WeatherLocation
//import com.example.forecast.data.db.Entity.CurrentWeatherEntry
//import com.example.forecast.data.db.Entity.currentWeather.WeatherLocation
//import com.example.forecast.data.db.Entity.CurrentWeatherResponse
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.forecast.data.db.dao.CurrentWeatherDAO
import com.example.forecast.data.db.dao.FutureWeatherDAO
import com.example.forecast.data.db.dao.WeatherLocationDAO
import com.example.forecast.data.db.entity.WeatherLocation
import com.example.forecast.data.db.entity.currentWeather.CurrentWeatherEntry
import com.example.forecast.data.db.specificFutureWeatherEntries.DetailedFutureWeatherEntries
import com.example.forecast.data.db.specificFutureWeatherEntries.SimpleFutureWeatherEntries
import com.example.forecast.data.network.WeatherNetworkDataSource
import com.example.forecast.data.network.response.currentWeather.CurrentWeatherResponse
import com.example.forecast.data.network.response.futureWeather.FutureWeatherResponse
import com.example.forecast.data.providers.LocationProvider
import com.example.forecast.data.providers.UnitProvider
import com.example.forecast.internal.UnitSystem
import kotlinx.coroutines.*
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime

const val FORECAST_DAYS_COUNT = 7
@DelicateCoroutinesApi
class WeatherRepositoryImpl(
    private val currentWeatherDAO: CurrentWeatherDAO,
    private val futureWeatherDAO: FutureWeatherDAO,
    private val weatherLocationDAO: WeatherLocationDAO,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val unitProvider: UnitProvider,
    private val locationProvider: LocationProvider
) : WeatherRepository {

    init {
        weatherNetworkDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeatherResponse ->
                persistCurrentWeather(newCurrentWeatherResponse)
            }
            downloadedFutureWeather.observeForever { newFutureWeatherResponse ->
                persistFutureWeather(newFutureWeatherResponse)
            }
        }
    }

    private val unit: UnitSystem
        get() = unitProvider.getUnitSystem()

    private val isUnitChanged: Boolean
        get() = unitProvider.onUnitChange(unit)

    @SuppressLint("LongLogTag")
    @DelicateCoroutinesApi
    private fun persistCurrentWeather(newCurrentWeatherResponse: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDAO.upsert(newCurrentWeatherResponse.currentWeatherEntry)
            val weatherLocation = WeatherLocation(
                newCurrentWeatherResponse.latitude,
                newCurrentWeatherResponse.longitude,
                newCurrentWeatherResponse.address,
                newCurrentWeatherResponse.timezone,
                newCurrentWeatherResponse.currentWeatherEntry.datetimeEpoch
            )
            weatherLocationDAO.upsert(weatherLocation)
        }
    }

    private fun persistFutureWeather(futureWeatherResponse: FutureWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            futureWeatherDAO.removeOldEntries(LocalDate.now())
            futureWeatherDAO.upsert(futureWeatherResponse.entries)
            val weatherLocation = WeatherLocation(
                futureWeatherResponse.latitude,
                futureWeatherResponse.longitude,
                futureWeatherResponse.address,
                futureWeatherResponse.timezone,
                LocalDate.now().toEpochDay()
                )
            weatherLocationDAO.upsert(weatherLocation)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getCurrentWeather(): LiveData<out CurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            updateWeather()
            currentWeatherDAO.getCurrentWeather()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getFutureWeatherList(startDate: LocalDate): LiveData<out List<SimpleFutureWeatherEntries>> {
        return withContext(Dispatchers.IO) {
            updateWeather()
            return@withContext futureWeatherDAO.getFutureWeatherForecast(startDate)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getDetailedFutureWeatherByDate(date: LocalDate): LiveData<out DetailedFutureWeatherEntries> {
        return withContext(Dispatchers.IO){
            updateWeather()
            return@withContext futureWeatherDAO.getDetailedFutureWeatherByDate(date)
        }
    }

    override suspend fun getWeatherLocation(): LiveData<out WeatherLocation> {
        return withContext(Dispatchers.IO) {
            weatherLocationDAO.getWeatherLocation()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun updateWeather() {
        val lastWeatherLocation = weatherLocationDAO.getWeatherLocationNonLive()
        if (lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather(locationProvider.getPreferredLocation(), unit)
            fetchFutureWeather(locationProvider.getPreferredLocation(),unit)
            return
        }
        if (isFetchCurrentWeatherNeeded(lastWeatherLocation.date)) {
            fetchCurrentWeather(locationProvider.getPreferredLocation(), unit)
        }

        if (isFetchFutureWeatherNeeded()) {
            fetchFutureWeather(locationProvider.getPreferredLocation(), unit)
        }

        if (isUnitChanged){
            fetchCurrentWeather(locationProvider.getPreferredLocation(), unit)
            fetchFutureWeather(locationProvider.getPreferredLocation(), unit)
        }
    }

    private suspend fun fetchCurrentWeather(location: String, unit: UnitSystem) {
        weatherNetworkDataSource.fetchCurrentWeather(location, unit.getUnitName()!!)
    }

    private suspend fun fetchFutureWeather(location: String, unit: UnitSystem){
        weatherNetworkDataSource.fetchFutureWeather(location, unit.getUnitName()!!)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isFetchCurrentWeatherNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val twentyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(twentyMinutesAgo)
    }

    private fun isFetchFutureWeatherNeeded(): Boolean {
        val today= LocalDate.now()
        val futureWeatherCount = futureWeatherDAO.countFutureWeather(today)
        return futureWeatherCount < FORECAST_DAYS_COUNT
    }

    override fun getDeviceLocationName(
        deviceLocationLat: Double,
        deviceLocationLon: Double
    ): String {
        return locationProvider.getDeviceLocationName(deviceLocationLat, deviceLocationLon)
    }

    override fun isMetric(): Boolean {
        return unitProvider.getUnitSystem() == UnitSystem.METRIC
    }
}