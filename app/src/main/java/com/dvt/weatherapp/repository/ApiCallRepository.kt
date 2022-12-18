package com.dvt.weatherapp.repository

import android.util.Log
import com.dvt.weatherapp.models.CurrentResponseModel
import com.dvt.weatherapp.models.ForecastResponseModel
import com.dvt.weatherapp.network.WebService
import retrofit2.Response

class ApiCallRepository {

    suspend fun fetchCurrentWeather(params: MutableMap<String, String>) : Response<CurrentResponseModel> {
        Log.d("resRepository", WebService.api.fetchCurrentWeather(params).toString())
        return WebService.api.fetchCurrentWeather(params)
    }

    suspend fun fetchForecastWeather(params: MutableMap<String, String>) : Response<ForecastResponseModel> {
        Log.d("resRepository", WebService.api.fetchForecastWeather(params).toString())
        return WebService.api.fetchForecastWeather(params)
    }

    /*
    fun fetchCurrentWeather(params: MutableMap<String, String>): LiveData<CurrentResponseModel> {
        val data = MutableLiveData<CurrentResponseModel?>()

        try {
            WebService.api.fetchCurrentWeather(params).enqueue(
                object : Callback<CurrentResponseModel> {
                    override fun onFailure(call: Call<CurrentResponseModel>, t: Throwable) {
                        Log.e("apiRepositoryError", "$t")
                    }

                    override fun onResponse(
                        call: Call<CurrentResponseModel>,
                        response: Response<CurrentResponseModel>
                    ) {
                        if (response.code() == 200) {
                            data.value =  response.body()
                        } else {
                            Log.e("apiRepositoryError", "${response.body()}")
                        }
                    }
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }
    */
}