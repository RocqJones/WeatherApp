package com.dvt.weatherapp

import android.app.Application
import com.dvt.weatherapp.repository.CurrentRoomRepository
import com.dvt.weatherapp.room.db.WeatherRoomDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BaseApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { WeatherRoomDb.getDatabase(this, applicationScope) }
    val currentRepository by lazy { CurrentRoomRepository(database.currentDao()) }
}