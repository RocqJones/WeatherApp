package com.dvt.weatherapp.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dvt.weatherapp.room.dao.CurrentDao
import com.dvt.weatherapp.room.entities.CurrentWeatherModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [CurrentWeatherModel::class], version = 1, exportSchema = false)
abstract class WeatherRoomDb : RoomDatabase() {
    abstract fun currentDao() : CurrentDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE : WeatherRoomDb? = null

        // To launch a coroutine you need a CoroutineScope
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ) : WeatherRoomDb {
            // If the INSTANCE is not null, then return it, if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherRoomDb::class.java,
                    "fs_db" // db name
                ).addCallback(RoomDatabaseCallback(scope)).build()

                INSTANCE = instance

                // return instance
                instance
            }
        }
    }

    /** Custom implementation of the RoomDatabase.Callback(). */
    class RoomDatabaseCallback(private val scope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { _ -> scope.launch {} }
        }
    }
}