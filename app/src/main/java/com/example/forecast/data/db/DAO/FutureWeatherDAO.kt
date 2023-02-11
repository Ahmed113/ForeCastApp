package com.example.forecast.data.db.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.forecast.data.db.Converters.DateConverter
import com.example.forecast.data.db.Entity.futureWeather.FutureWeatherEntry
import com.example.forecast.data.db.specificFutureWeatherEntries.DetailedFutureWeatherEntries
import com.example.forecast.data.db.specificFutureWeatherEntries.SimpleFutureWeatherEntries
import org.threeten.bp.LocalDate

@Dao
@TypeConverters(DateConverter::class)
interface FutureWeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(futureWeatherEntry: List<FutureWeatherEntry>)

    @Query("Select * from future_weather Where date(datetime) > date(:startDate) order by datetime ")
    fun getFutureWeatherForecast(startDate: LocalDate): LiveData<List<SimpleFutureWeatherEntries>>

    @Query("Select * from future_weather Where date(datetime) = date(:startDate)")
    fun getDetailedFutureWeatherByDate(startDate: LocalDate): LiveData<DetailedFutureWeatherEntries>

    @Query("Select Count(id) from future_weather Where datetime(datetime) > datetime(:startDate)")
    fun countFutureWeather(startDate: LocalDate): Int

    @Query("Delete from future_weather where datetime(datetime) <= datetime(:firstDateToKeep) ")
    fun removeOldEntries(firstDateToKeep: LocalDate)

}