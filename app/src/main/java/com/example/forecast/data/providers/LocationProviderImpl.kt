package com.example.forecast.data.providers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
//import com.example.forecast.InvalidLocation
import com.example.forecast.LocationPermissionGrantedException
import com.example.forecast.data.db.Entity.WeatherLocation
//import com.example.forecast.data.db.Entity.currentWeather.WeatherLocation
//import com.example.forecast.data.db.Entity.WeatherLocation
import com.example.forecast.internal.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import org.threeten.bp.ZoneId
import java.util.*

const val DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : PreferencesProvider(context), LocationProvider {

    private val appContext: Context = context.applicationContext
    override suspend fun hasLocationChanged(lastLocation: WeatherLocation): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastLocation)
        } catch (e: LocationPermissionGrantedException) {
            false
        }
        return deviceLocationChanged || hasCustomLocationChanged(lastLocation)
    }

    override suspend fun getPreferredLocation(): String {
        if (isUsingDeviceLocation()) {
            try {
                val deviceLocation =
                    getLastDeviceLocation().await() ?: return "${getCustomLocationName()}"
//                return getDeviceLocationName(deviceLocation)
                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            } catch (e: LocationPermissionGrantedException) {
                return "${getCustomLocationName()}"
            }
        } else{
            return "${getCustomLocationName()}"
        }
    }

    override fun getDeviceLocationName(deviceLocationLat: Double, deviceLocationLon: Double): String {
//        val deviceLocation = getLastDeviceLocationAsync().await()
        val geoCoder = Geocoder(appContext, Locale.getDefault())
        val address = geoCoder.getFromLocation(deviceLocationLat, deviceLocationLon, 1)
        if (address.size > 0) {
            val city = address[0].adminArea
            Log.d("TAGLocCity", "getDeviceLocationName:$city ")
            return city
        }else
        return "$deviceLocationLat,$deviceLocationLon"
    }

    private suspend fun hasDeviceLocationChanged(lastLocation: WeatherLocation): Boolean {
        if (!isUsingDeviceLocation()) {
            return false
        }
        val deviceLocation = getLastDeviceLocation().await() ?: return false
        val comparisonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - lastLocation.latitude) > comparisonThreshold &&
                Math.abs(deviceLocation.longitude - lastLocation.longitude) > comparisonThreshold
    }

    private fun hasCustomLocationChanged(lastLocation: WeatherLocation): Boolean {
        if (!isUsingDeviceLocation()) {
            val customLocationName = getCustomLocationName()
            return customLocationName != lastLocation.cityName
        }else
            return false
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(DEVICE_LOCATION, true)
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        if (hasLocationPermission()) {
            return fusedLocationProviderClient.lastLocation.asDeferred()
        } else
            throw LocationPermissionGrantedException()

    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}

