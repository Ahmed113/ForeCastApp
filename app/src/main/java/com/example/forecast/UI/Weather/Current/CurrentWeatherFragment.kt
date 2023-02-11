package com.example.forecast.UI.Weather.Current

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.forecast.data.db.Entity.WeatherLocation
import com.example.forecast.databinding.CurrentWeatherFragmentBinding
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*

const val ICONURL =
    "https://raw.githubusercontent.com/visualcrossing/WeatherIcons/main/PNG/3rd%20Set%20-%20Color/"

class CurrentWeatherFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    val viewModelFactory: CurrentWeatherVMFactory by instance()

    private lateinit var binding: CurrentWeatherFragmentBinding
    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CurrentWeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("FragmentLiveDataObserve", "UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)
        bindUI()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun bindUI() {
        lifecycleScope.launch(Dispatchers.Main) {

            val weatherLocation = viewModel.getWeatherLocation().await()
            val currentWeather = viewModel.getCurrentWeather().await()

            weatherLocation.observe(this@CurrentWeatherFragment, Observer { location ->
                if (location == null) return@Observer
                updateLocation(location)
                Log.d("unit location", "onViewCreated: ${location.cityName}")
            })

            currentWeather.observe(this@CurrentWeatherFragment, Observer { weather ->
                if (weather == null) return@Observer
                binding.groupLoading.visibility = View.GONE
                updateCondition(weather.conditions)
                updateTemperature(weather.temp, weather.feelslike)
                updateDateTotoday()
                updateWind(weather.windspeed)
                updateVisibility(weather.visibility)
                updateHumidity(weather.humidity)
                Glide.with(this@CurrentWeatherFragment)
                    .load("$ICONURL${weather.icon}.png")
                    .into(binding.weathericon)
            })
        }
    }

    //https://raw.githubusercontent.com/visualcrossing/WeatherIcons/main/PNG/3rd%20Set%20-%20Color/clear-day.png
    private fun updateLocation(location: WeatherLocation) {
        if (location.cityName.contains("[0-9]".toRegex())) {
            val locationName =
                viewModel.getDeviceLocationName(location.latitude, location.longitude)
            (activity as? AppCompatActivity)?.supportActionBar?.title = locationName
        } else
            (activity as? AppCompatActivity)?.supportActionBar?.title = location.cityName
    }

    private fun updateDateTotoday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "today"
    }

    private fun updateUnit(metric: String, us: String): String {
        if (viewModel.isMetric()) {
            return metric
        } else {
            return us
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateCondition(condition: String) {
        binding.weatherCondition.text = condition
    }

    @SuppressLint("SetTextI18n")
    private fun updateTemperature(temperature: Double, feelsLike: Double) {
        val unit = updateUnit("°C", "°F")
        binding.temperature.text = "$temperature$unit"
        binding.feels.text = "Feels Like:$feelsLike$unit"
    }

    @SuppressLint("SetTextI18n")
    private fun updateWind(wind: Double) {
        val unit = updateUnit("kph", "mph")
        binding.wind.text = "Wind: $wind $unit "
    }

    @SuppressLint("SetTextI18n")
    private fun updateVisibility(visibility: Double) {
        val unit = updateUnit("km", "mi")
        binding.visibility.text = "Visibility: $visibility $unit"
    }

    @SuppressLint("SetTextI18n")
    private fun updateHumidity(humidity: Double) {
        binding.humidity.text = "Humidity: $humidity %"
    }
}