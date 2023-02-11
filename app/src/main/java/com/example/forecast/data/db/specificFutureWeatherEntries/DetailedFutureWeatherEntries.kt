package com.example.forecast.data.db.specificFutureWeatherEntries

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate


class DetailedFutureWeatherEntries (
    @ColumnInfo(name="datetime")
    @SerializedName("datetime")
    val datetime: LocalDate,
    @ColumnInfo(name="tempmax")
    val maximumTemperature: Double,
    @ColumnInfo(name="tempmin")
    val minimumTemperature: Double,
    @ColumnInfo(name="temp")
    val avTemperature: Double,
    @ColumnInfo(name="conditions")
    val conditionText: String,
    @ColumnInfo(name="icon")
    val conditionIcon: String,
    val humidity: Double,
    val uvindex: Double,
    val windspeed: Double,
    val winddir: Double,
    val visibility: Double,
    val description: String

    )