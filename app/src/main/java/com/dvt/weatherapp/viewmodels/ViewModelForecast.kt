package com.dvt.weatherapp.viewmodels

import androidx.lifecycle.*
import com.dvt.weatherapp.repository.ForecastRoomRepository
import com.dvt.weatherapp.room.entities.ForecastWeatherModel
import kotlinx.coroutines.launch

class ViewModelForecast(private val repository: ForecastRoomRepository) : ViewModel() {
    val getAllForecastWeather : LiveData<List<ForecastWeatherModel>> =
        repository.getAllForecastWeather.asLiveData()

    // Launching a new coroutine to insert the data in a non-blocking way
    fun insert(forecastWeatherModel: ForecastWeatherModel) = viewModelScope.launch {
        repository.insert(forecastWeatherModel)
    }

    fun deleteForecastDetails() = viewModelScope.launch {
        repository.deleteForecastDetails()
    }

    fun update(_id: Int, status: String) = viewModelScope.launch{
        repository.update(_id, status)
    }
}
class ForecastViewModelFactory(private val repository: ForecastRoomRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelForecast::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelForecast(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}