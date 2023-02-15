package com.example.forecast.ui.weather.future.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.forecast.data.db.converters.DateConverter
import com.example.forecast.data.db.entity.WeatherLocation
import com.example.forecast.data.network.ICONURL
import com.example.forecast.databinding.FutureWeatherDetailsFragmentBinding
import com.example.forecast.internal.UnitSystem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.factory
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class FutureWeatherDetailsFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    val viewModelFactoryInstanceFactory:
            ((LocalDate) -> FutureWeatherDetailsVMFactory) by factory()

    private lateinit var binding: FutureWeatherDetailsFragmentBinding
    private lateinit var viewModel: FutureWeatherDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FutureWeatherDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.futuer_weather_details_fragment, container, false)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val safeArgs = arguments?.let {
            FutureWeatherDetailsFragmentArgs.fromBundle(it) }
        val date = DateConverter.stringToDate(safeArgs?.dateString!!)
        viewModel = ViewModelProvider(this, viewModelFactoryInstanceFactory(date)).get(
            FutureWeatherDetailsViewModel::class.java
        )
        bindUI()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun bindUI() {
        lifecycleScope.launch(Dispatchers.Main) {

            val futureWeather = viewModel.getDetailsOfFutureWeatherByDate().await()
            val weatherLocation = viewModel.getWeatherLocation().await()

            weatherLocation.observe(this@FutureWeatherDetailsFragment, Observer { location ->
                if (location == null) return@Observer
                updateLocation(location)
                Log.d("unit location", "onViewCreated: ${location.cityName}")
            })

            futureWeather.observe(this@FutureWeatherDetailsFragment, Observer { weatherEntry ->
                if (weatherEntry == null) return@Observer
//                tv_visibility.text = weatherEntry.dt.toString()
                binding.groupDetailsLoading.visibility = View.GONE
                updateCondition(weatherEntry.description)
                updatetemperatur(
                    weatherEntry.avTemperature,
                    weatherEntry.minimumTemperature,
                    weatherEntry.maximumTemperature
                )
                updateDate(weatherEntry.datetime)
                updateWind(weatherEntry.windspeed)
                updateVisibility(weatherEntry.visibility)
                updateHumidity(weatherEntry.humidity)
                updateUv(weatherEntry.uvindex)
                Glide.with(this@FutureWeatherDetailsFragment)
                    .load("$ICONURL${weatherEntry.conditionIcon}.png")
                    .into(binding.weathericon)
//                    Log.d("TAG333", "bindUI: ${weatherEntry}")
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

    private fun updateDate(date: LocalDate) {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
            date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }

    private fun updateUnit(metric: String, us: String): String {
        if (viewModel.isMetric()) {
            Log.d("unitmk", "updateUnit: ${viewModel.isMetric()} ::: ${UnitSystem.METRIC.name}")
            return metric
        } else {
            Log.d("unitmk", "updateUnit: ${viewModel.isMetric()} ::: ${UnitSystem.US.name}")
            return us
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateCondition(condition: String) {
        binding.weatherDescription.text = condition
    }

    @SuppressLint("SetTextI18n")
    private fun updatetemperatur(temperature: Double, tempMin: Double, tempMax: Double) {
        val unit = updateUnit("°C", "°F")
        binding.avTemperature.text = "$temperature$unit"
        binding.minValue.text = "$tempMin$unit"
        binding.maxValue.text = "$tempMax$unit"
    }

    @SuppressLint("SetTextI18n")
    private fun updateWind(wind: Double) {
        val unit = updateUnit("kph", "mph")
        binding.fWind.text = "Wind: $wind $unit "
    }

    @SuppressLint("SetTextI18n")
    private fun updateVisibility(visibility: Double) {
        val unit = updateUnit("km", "mi")
        binding.fVisibility.text = "Visibility: $visibility $unit"
    }

    @SuppressLint("SetTextI18n")
    private fun updateHumidity(humidity: Double) {
        binding.fHumidity.text = "Humidity:$humidity %"
    }

    @SuppressLint("SetTextI18n")
    private fun updateUv(uvindex: Double) {
        binding.uvIndex.text = "UV: $uvindex"
    }
}