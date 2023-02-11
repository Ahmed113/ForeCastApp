package com.example.forecast.data.db.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecast.data.db.Entity.currentWeather.CURRENT_WEATHER_ID
import com.example.forecast.data.db.Entity.currentWeather.CurrentWeatherEntry

@Dao
//@TypeConverters(ListConverter::class)
interface CurrentWeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(currentWeatherEntry: CurrentWeatherEntry)

    @Query("Select * from Current_Weather where weather_id = $CURRENT_WEATHER_ID")
    fun getCurrentWeather(): LiveData<CurrentWeatherEntry>
}