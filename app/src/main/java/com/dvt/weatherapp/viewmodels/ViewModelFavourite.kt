package com.dvt.weatherapp.viewmodels

import androidx.lifecycle.*
import com.dvt.weatherapp.repository.FavouriteRoomRepository
import com.dvt.weatherapp.room.entities.FavouriteWeatherModel
import kotlinx.coroutines.launch

class ViewModelFavourite(private val repository: FavouriteRoomRepository) : ViewModel() {

    val getAllFavouriteWeather : LiveData<List<FavouriteWeatherModel>> =
        repository.getAllFavouriteWeather.asLiveData()

    // Launching a new coroutine to insert the data in a non-blocking way
    fun insert(favouriteWeatherModel: FavouriteWeatherModel) = viewModelScope.launch {
        repository.insert(favouriteWeatherModel)
    }

    fun deleteFavouriteItem(_id: Int) = viewModelScope.launch {
        repository.deleteFavouriteItem(_id)
    }
}
class FavouriteViewModelFactory(private val repository: FavouriteRoomRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelFavourite::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelFavourite(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}