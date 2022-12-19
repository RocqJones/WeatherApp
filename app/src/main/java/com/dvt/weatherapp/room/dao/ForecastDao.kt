package com.dvt.weatherapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dvt.weatherapp.room.entities.ForecastWeatherModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {
    @Query("SELECT * FROM t_forecast ORDER BY id ASC")
    fun getAllForecastWeather(): Flow<List<ForecastWeatherModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(forecastWeatherModel: ForecastWeatherModel)

    @Query("DELETE FROM t_forecast")
    suspend fun deleteForecastDetails()

    @Query("UPDATE t_forecast SET liked = :status WHERE id = :id")
    suspend fun update(id: Int, status: String)
}