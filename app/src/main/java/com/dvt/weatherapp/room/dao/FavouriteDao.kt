package com.dvt.weatherapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dvt.weatherapp.room.entities.FavouriteWeatherModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM t_favourite ORDER BY id ASC")
    fun getAllFavouriteWeather(): Flow<List<FavouriteWeatherModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favouriteWeatherModel: FavouriteWeatherModel)

    @Query("DELETE FROM t_favourite WHERE id = :id")
    suspend fun deleteFavouriteItem(id: Int)
}