package com.dvt.weatherapp.repository

import androidx.annotation.WorkerThread
import com.dvt.weatherapp.room.dao.CurrentDao
import com.dvt.weatherapp.room.entities.CurrentWeatherModel
import kotlinx.coroutines.flow.Flow

class CurrentRoomRepository(private val currentDao: CurrentDao) {

    val getAllCurrentWeather : Flow<List<CurrentWeatherModel>> = currentDao.getAllCurrentWeather()

    @WorkerThread
    suspend fun insert(currentWeatherModel: CurrentWeatherModel) {
        currentDao.insert(currentWeatherModel)
    }

    @WorkerThread
    suspend fun deleteCurrentDetails() {
        currentDao.deleteCurrentDetails()
    }
}