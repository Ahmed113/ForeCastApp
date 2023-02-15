package com.example.forecast.data.db.entity.futureWeather


import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.forecast.data.db.converters.DateConverter

@Entity(tableName = "future_weather", indices = [Index(value=["datetime"],unique=true)])
@TypeConverters(DateConverter::class)
data class FutureWeatherEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val datetime: String,
    val datetimeEpoch: Long,
    val tempmax: Double,
    val tempmin: Double,
    val temp: Double,
    val feelslikemax: Double,
    val feelslikemin: Double,
    val feelslike: Double,
    val dew: Double,
    val humidity: Double,
    val precip: Double,
    val precipprob: Double,
    val precipcover: Double,
//    val preciptype: Any,
    val snow: Double,
    val snowdepth: Double,
    val windgust: Double,
    val windspeed: Double,
    val winddir: Double,
    val pressure: Double,
    val cloudcover: Double,
    val visibility: Double,
    val solarradiation: Double,
    val solarenergy: Double,
    val uvindex: Double,
    val severerisk: Double,
    val sunrise: String,
    val sunriseEpoch: Int,
    val sunset: String,
    val sunsetEpoch: Int,
    val moonphase: Double,
    val conditions: String,
    val description: String,
    val icon: String,
//    val stations: Any,
    val source: String
)