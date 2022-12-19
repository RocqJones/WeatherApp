package com.dvt.weatherapp.repository

import androidx.annotation.WorkerThread
import com.dvt.weatherapp.room.dao.ForecastDao
import com.dvt.weatherapp.room.entities.ForecastWeatherModel
import kotlinx.coroutines.flow.Flow

class ForecastRoomRepository(private val forecastDao: ForecastDao) {
    val getAllForecastWeather : Flow<List<ForecastWeatherModel>> = forecastDao.getAllForecastWeather()

    @WorkerThread
    suspend fun insert(forecastWeatherModel: ForecastWeatherModel) {
        forecastDao.insert(forecastWeatherModel)
    }

    @WorkerThread
    suspend fun deleteForecastDetails() {
        forecastDao.deleteForecastDetails()
    }

    @WorkerThread
    suspend fun update(_id: Int, status: String) {
        forecastDao.update(_id, status)
    }
}