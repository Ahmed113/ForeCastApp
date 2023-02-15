package com.example.forecast.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecast.data.db.entity.WeatherLocation

@Dao
interface WeatherLocationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(weatherLocation: WeatherLocation)

    @Query("Select * from weather_location")
    fun getWeatherLocation(): LiveData<WeatherLocation>

    @Query("Select * from weather_location")
    fun getWeatherLocationNonLive(): WeatherLocation?
}