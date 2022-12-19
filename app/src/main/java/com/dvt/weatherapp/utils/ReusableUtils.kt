package com.dvt.weatherapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ReusableUtils {

    // Check if device has internet
    @Suppress("DEPRECATION")
    fun isDeviceConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    fun convertKelvinToCelsius(k: Double): Double {
        val c = 273.15
        val res = k - c
        // ans to 1 decimal place
        return String.format("%.1f", res).toDouble()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateTimeConverter(dateTimeStr: String): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy")
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
        val zonedDateTime = ZonedDateTime.parse("$dateTimeStr Z", pattern)
        // println("Date Time $zonedDateTime")
        // println("Date Time ${formatter.format(zonedDateTime)}")
        return formatter.format(zonedDateTime)
    }
}