package com.example.forecast.data.network

//import com.example.forecast.data.db.Entity.CurrentWeatherResponse
import com.example.forecast.data.network.response.currentWeather.CurrentWeatherResponse
import com.example.forecast.data.network.response.futureWeather.FutureWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/weather?q=London&appid=53ff0d350f5c2f94c3e441ee5bf4a45c
//https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/london/today?unitGroup=metric&include=current&key=6GA5TMVFLE6ZKU4SEER4Q2HL4&contentType=json
//https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/london/next7days?unitGroup=metric&include=current&key=6GA5TMVFLE6ZKU4SEER4Q2HL4&contentType=json

interface WeatherAPIService {
    @GET("{location}/today")
    suspend fun getCurrntWeather(
        @Path("location") location: String,
        @Query("unitGroup") unit: String
    ): CurrentWeatherResponse

    @GET("{location}/next7days")
    suspend fun getFutureWeather(
        @Path("location") location: String,
        @Query("unitGroup") unit: String
    ): FutureWeatherResponse
}