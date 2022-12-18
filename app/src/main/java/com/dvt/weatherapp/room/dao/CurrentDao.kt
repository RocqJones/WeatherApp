package com.dvt.weatherapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dvt.weatherapp.room.entities.CurrentWeatherModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentDao {
    @Query("SELECT * FROM t_current ORDER BY id ASC")
    fun getAllCurrentWeather(): Flow<CurrentWeatherModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentWeatherModel: CurrentWeatherModel)

    @Query("DELETE FROM t_current")
    suspend fun deleteCurrentDetails()
}