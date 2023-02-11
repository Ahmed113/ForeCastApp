package com.example.forecast.UI

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority

class LifeCycleBoundLocationManager(
    lifecycleOwner: LifecycleOwner,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationCallback: LocationCallback
) : DefaultLifecycleObserver {
    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    private val locationRequest =
        LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 5000)
            .setMinUpdateIntervalMillis(5000)
            .setWaitForAccurateLocation(false)
            .build()


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun removeLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        startLocationUpdates()
        Log.d("TAGLoc", "startLocationUpdates: Location Updated")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        removeLocationUpdates()
        Log.d("TAGLoc", "startLocationUpdates: Location Removed")

    }
}