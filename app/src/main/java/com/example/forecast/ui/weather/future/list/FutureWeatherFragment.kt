package com.example.forecast.ui.weather.future.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecast.data.db.converters.DateConverter
import com.example.forecast.data.db.entity.WeatherLocation
import com.example.forecast.data.db.specificFutureWeatherEntries.SimpleFutureWeatherEntries
import com.example.forecast.databinding.FutureWeatherFragmentBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDate

class FutureWeatherFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val futureWeatherVMFactory: FutureWeatherVMFactory by instance()

    private lateinit var binding: FutureWeatherFragmentBinding
    private lateinit var viewModel: FutureWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FutureWeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.future_weather_fragment, container, false)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(this, futureWeatherVMFactory).get(FutureWeatherViewModel::class.java)

        lifecycleScope.launch(Dispatchers.Main) {
            val simpleFutureWeatherEntry = viewModel.getFutureWeather().await()
            val weatherLocation = viewModel.getWeatherLocation().await()

            simpleFutureWeatherEntry.observe(this@FutureWeatherFragment,Observer { weatherEntries ->
                if (weatherEntries == null)return@Observer
                    binding.listLoading.visibility = View.GONE
                    updateDateToNextWeek()
                    initRecyclerView(weatherEntries.toFutureWeatherItems())
                })

            weatherLocation.observe(this@FutureWeatherFragment, Observer { location ->
                if (location == null) return@Observer
                updateLocation(location)
            })
        }
    }

    private fun updateLocation(location: WeatherLocation) {
        if (location.cityName.contains("[0-9]".toRegex())) {
            val locationName =
                viewModel.getDeviceLocationName(location.latitude, location.longitude)
            (activity as? AppCompatActivity)?.supportActionBar?.title = locationName
        } else
            (activity as? AppCompatActivity)?.supportActionBar?.title = location.cityName
    }

    private fun updateDateToNextWeek() {
        if (viewModel.isMetric())
            (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Next Week '°C'"
        else
            (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Next Week '°F'"
    }

    private fun List<SimpleFutureWeatherEntries>.toFutureWeatherItems(): List<FutureWeatherItem> {
        return this.map {
            FutureWeatherItem(it)
        }
    }

    private fun initRecyclerView(items: List<FutureWeatherItem>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FutureWeatherFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? FutureWeatherItem)?.let {
                showWeatherDetails(it.weatherEntry.datetime, view) }
        }
    }

    private fun showWeatherDetails(date: LocalDate, view: View) {
        val dateString = DateConverter.dateToString(date)
        val actionDetail = FutureWeatherFragmentDirections.actionDetail(dateString)
        Navigation.findNavController(view).navigate(actionDetail)
    }
}
