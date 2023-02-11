package com.example.forecast

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.example.forecast.UI.Weather.Current.CurrentWeatherVMFactory
import com.example.forecast.UI.Weather.Future.Details.FutureWeatherDetailsVMFactory
import com.example.forecast.UI.Weather.Future.List.FutureWeatherVMFactory
import com.example.forecast.data.Repo.WeatherRepositoryImpl
import com.example.forecast.data.network.WeatherAPIClient
import com.example.forecast.data.network.WeatherAPIService
import com.example.forecast.data.db.ForecastDB
import com.example.forecast.data.network.ConnectivityInterceptor
import com.example.forecast.data.network.ConnectivityInterceptorImpl
import com.example.forecast.data.network.WeatherNetworkDataSource
import com.example.forecast.data.network.WeatherNetworkDataSourceImpl
import com.example.forecast.data.providers.LocationProvider
import com.example.forecast.data.providers.LocationProviderImpl
import com.example.forecast.data.providers.UnitProvider
import com.example.forecast.data.providers.UnitProviderImpl
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.DelicateCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*
import org.threeten.bp.LocalDate

class ForecastApplication : Application(), KodeinAware {

    @OptIn(DelicateCoroutinesApi::class)
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))
        bind() from singleton { ForecastDB.getDatabase(instance()) }
        bind() from singleton { instance<ForecastDB>().currentWeatherDAO() }
        bind() from singleton { instance<ForecastDB>().futureWeatherDAO() }
        bind() from singleton { instance<ForecastDB>().weatherLocationDAO() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind<WeatherAPIService>() with singleton { WeatherAPIClient(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(),instance()) }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { CurrentWeatherVMFactory(instance()) }
        bind() from factory { date : LocalDate -> FutureWeatherDetailsVMFactory(instance(), date) }
        bind() from provider { FutureWeatherVMFactory(instance()) }
        bind() from singleton { WeatherRepositoryImpl(instance(),instance(),instance(),instance(),instance(),instance()) }
    }
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this,R.xml.preferences,false)
    }
}