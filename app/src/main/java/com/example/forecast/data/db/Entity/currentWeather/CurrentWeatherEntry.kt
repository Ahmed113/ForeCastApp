package com.example.forecast.data.db.Entity.currentWeather


import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "Current_Weather")
//@TypeConverters(DateConverter::class)
data class CurrentWeatherEntry(
    val datetime: String,
//    @SerializedName("datetimeEpoch")
    val datetimeEpoch: Long,
    val temp: Double,
    val feelslike: Double,
    val humidity: Double,
    val dew: Double,
    val precip: Double,
    val precipprob: Double,
    val snow: Double,
    val snowdepth: Double,
//    val preciptype: Any,
//    val windgust: Any,
    val windspeed: Double,
    val winddir: Double,
    val pressure: Double,
    val visibility: Double,
    val cloudcover: Double,
    val conditions: String,
    val icon: String,
    val sunrise: String,
    val sunriseEpoch: Int,
    val sunset: String,
    val sunsetEpoch: Int
) {
    @PrimaryKey(autoGenerate = false)
    var weather_id: Int = CURRENT_WEATHER_ID
}