package com.dvt.weatherapp.listeners

import com.dvt.weatherapp.room.entities.ForecastWeatherModel

interface ForecastWeatherListener {
    fun onResponse(model: ForecastWeatherModel, i: Int)
}