package com.example.forecast.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.forecast.data.db.converters.DateConverter
import com.example.forecast.data.db.dao.CurrentWeatherDAO
import com.example.forecast.data.db.dao.FutureWeatherDAO
import com.example.forecast.data.db.dao.WeatherLocationDAO
import com.example.forecast.data.db.entity.WeatherLocation
//import com.example.forecast.data.db.Entity.CurrentWeatherEntry
//import com.example.forecast.data.db.Entity.CurrentWeatherResponse
//import com.example.forecast.data.db.Entity.WeatherLocation
import com.example.forecast.data.db.entity.currentWeather.CurrentWeatherEntry
import com.example.forecast.data.db.entity.futureWeather.FutureWeatherEntry

//import com.example.forecast.data.db.Entity.currentWeather.WeatherLocation

@Database(entities = [CurrentWeatherEntry::class,FutureWeatherEntry::class, WeatherLocation::class], version = 31, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class ForecastDB : RoomDatabase() {

    abstract fun currentWeatherDAO(): CurrentWeatherDAO
    abstract fun futureWeatherDAO(): FutureWeatherDAO
    abstract fun weatherLocationDAO(): WeatherLocationDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ForecastDB? = null

        fun getDatabase(context: Context): ForecastDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ForecastDB::class.java,
                    "weather_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}