package com.dvt.weatherapp.viewmodels

import androidx.lifecycle.*
import com.dvt.weatherapp.repository.CurrentRoomRepository
import com.dvt.weatherapp.room.entities.CurrentWeatherModel
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ViewModelCurrent(private val repository: CurrentRoomRepository) : ViewModel() {
    val getAllCurrentWeather: LiveData<CurrentWeatherModel> =
        repository.getAllCurrentWeather.asLiveData()

    // Launching a new coroutine to insert the data in a non-blocking way
    fun insert(currentWeatherModel: CurrentWeatherModel) = viewModelScope.launch {
        repository.insert(currentWeatherModel)
    }

    fun deleteCurrentDetails() = viewModelScope.launch {
        repository.deleteCurrentDetails()
    }
}

class CurrentViewModelFactory(private val repository: CurrentRoomRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelCurrent::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelCurrent(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}