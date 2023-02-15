package com.example.forecast.data.network

//import okhttp3.internal.closeQuietly
import android.util.Log
//import com.example.forecast.data.network.ConnetivityInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/london/today?unitGroup=metric&include=current&key=6GA5TMVFLE6ZKU4SEER4Q2HL4&contentType=json
//https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/london/next7days?unitGroup=metric&include=current&key=6GA5TMVFLE6ZKU4SEER4Q2HL4&contentType=json
const val ICONURL =
    "https://raw.githubusercontent.com/visualcrossing/WeatherIcons/main/PNG/3rd%20Set%20-%20Color/"

object WeatherAPIClient {
    private const val Base_Url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
    private const val API_KEY = "6GA5TMVFLE6ZKU4SEER4Q2HL4"
    private const val PARAMETER_CURRENT = "current"

    operator fun invoke(
        connectivityInterceptor: ConnectivityInterceptor
    ): WeatherAPIService {
        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("key", API_KEY)
                .addQueryParameter("include", PARAMETER_CURRENT)
                .build()
//            Log.d("TAGinvoke", "invoke: $url")

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()
//            Log.d("TAGreq", "invoke: $request")
            return@Interceptor chain.proceed(request)
        }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(connectivityInterceptor)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .client(httpClient)
            .baseUrl(Base_Url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        return retrofit.create(WeatherAPIService::class.java)
    }
}