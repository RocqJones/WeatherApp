package com.dvt.weatherapp

import android.app.Application
import com.dvt.weatherapp.repository.CurrentRoomRepository
import com.dvt.weatherapp.repository.FavouriteRoomRepository
import com.dvt.weatherapp.repository.ForecastRoomRepository
import com.dvt.weatherapp.room.db.WeatherRoomDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BaseApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { WeatherRoomDb.getDatabase(this, applicationScope) }

    // Current
    val currentRepository by lazy { CurrentRoomRepository(database.currentDao()) }

    // Forecast
    val forecastRepository by lazy { ForecastRoomRepository(database.forecastDao()) }

    // Favourite
    val favouriteRepository by lazy { FavouriteRoomRepository(database.favouriteDao()) }
}