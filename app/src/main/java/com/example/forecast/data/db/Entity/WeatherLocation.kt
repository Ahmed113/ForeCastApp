package com.example.forecast.data.db.Entity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime


const val WEATHER_LOCATION_ID = 1

@Entity(tableName = "weather_location")
class WeatherLocation(
    val latitude: Double,
    val longitude: Double,
    val cityName: String,
    val timezone: String,
    val datetimeEpoch: Long
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = WEATHER_LOCATION_ID
    val date: ZonedDateTime
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            val zoneId = ZoneId.of(timezone)
            val instant = Instant.ofEpochSecond(datetimeEpoch)
            return ZonedDateTime.ofInstant(instant, zoneId)
        }
}

//    val zoneIds = ZoneId.getAvailableZoneIds()
