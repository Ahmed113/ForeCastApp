package com.example.forecast.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.example.forecast.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response
//import okhttp3.internal.closeQuietly

class ConnectivityInterceptorImpl(context: Context) : ConnectivityInterceptor {

    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline()){
//            Log.d("off1", "msg:${isOnline().toString()}")
            throw NoConnectivityException()
        }
//        Log.d("off", "msg:${isOnline()}")
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    fun isOnline(): Boolean {
        var result = false
        val connectivityManager =
            appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Log.d("sdk", "isOnline: ${Build.VERSION.SDK_INT}")
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNW =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNW.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNW.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNW.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        return result
    }
}