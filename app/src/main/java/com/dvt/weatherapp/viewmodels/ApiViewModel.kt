package com.dvt.weatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.dvt.weatherapp.models.CurrentResponseModel
import com.dvt.weatherapp.models.ForecastResponseModel
import com.dvt.weatherapp.repository.ApiCallRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.IllegalArgumentException

class ApiViewModel(private var repository: ApiCallRepository) : ViewModel()  {

    var fetchCurrentWeatherResponse : MutableLiveData<Response<CurrentResponseModel>> = MutableLiveData()
    var fetchForecastWeatherResponse : MutableLiveData<Response<ForecastResponseModel>> = MutableLiveData()

    fun fetchCurrentWeather(params: MutableMap<String, String>) {
        try {
            viewModelScope.launch {
                val response = repository.fetchCurrentWeather(params)
                fetchCurrentWeatherResponse.value = response
                Log.d("apiResViewModel", response.toString())
            }
        } catch (e: Exception) {
           e.printStackTrace()
        }
    }

    fun fetchForecastWeather(params: MutableMap<String, String>) {
        try {
            viewModelScope.launch {
                val response = repository.fetchForecastWeather(params)
                fetchForecastWeatherResponse.value = response
                Log.d("apiResViewModel", response.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
class ApiViewModelFactory(private val repository: ApiCallRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ApiViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}