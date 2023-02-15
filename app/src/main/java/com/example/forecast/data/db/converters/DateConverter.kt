package com.example.forecast.data.db.converters

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

object DateConverter {
    @TypeConverter
    @JvmStatic
    fun stringToDate(str: String): LocalDate {
        return LocalDate.parse(str, DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @TypeConverter
    @JvmStatic
    fun dateToString(date: LocalDate): String {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
}