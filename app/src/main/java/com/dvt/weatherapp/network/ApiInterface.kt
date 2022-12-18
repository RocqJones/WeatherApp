package com.dvt.weatherapp.network

import com.dvt.weatherapp.models.CurrentResponseModel
import com.dvt.weatherapp.models.ForecastResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * parse api param query and Response model
 */
interface ApiInterface {
    @GET("weather")
    suspend fun fetchCurrentWeather(@QueryMap params: MutableMap<String, String>): Response<CurrentResponseModel>

    @GET("forecast")
    suspend fun fetchForecastWeather(@QueryMap params: MutableMap<String, String>): Response<ForecastResponseModel>
}

