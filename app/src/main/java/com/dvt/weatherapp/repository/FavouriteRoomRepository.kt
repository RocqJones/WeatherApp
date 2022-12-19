package com.dvt.weatherapp.repository

import androidx.annotation.WorkerThread
import com.dvt.weatherapp.room.dao.FavouriteDao
import com.dvt.weatherapp.room.entities.FavouriteWeatherModel
import kotlinx.coroutines.flow.Flow

class FavouriteRoomRepository(private val favouriteDao: FavouriteDao) {

    val getAllFavouriteWeather : Flow<List<FavouriteWeatherModel>> = favouriteDao.getAllFavouriteWeather()

    @WorkerThread
    suspend fun insert(favouriteWeatherModel: FavouriteWeatherModel) {
        favouriteDao.insert(favouriteWeatherModel)
    }

    @WorkerThread
    suspend fun deleteFavouriteItem(_id: Int) {
        favouriteDao.deleteFavouriteItem(_id)
    }
}