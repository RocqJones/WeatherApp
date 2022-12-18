package com.dvt.weatherapp.views.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dvt.weatherapp.BaseApplication
import com.dvt.weatherapp.databinding.FragmentHomeBinding
import com.dvt.weatherapp.models.CurrentResponseModel
import com.dvt.weatherapp.models.ForecastResponseModel
import com.dvt.weatherapp.repository.ApiCallRepository
import com.dvt.weatherapp.room.entities.CurrentWeatherModel
import com.dvt.weatherapp.room.entities.ForecastWeatherModel
import com.dvt.weatherapp.utils.Constants
import com.dvt.weatherapp.utils.ReusableUtils
import com.dvt.weatherapp.viewmodels.*

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: ApiViewModel by viewModels {
        val repository = ApiCallRepository()
        ApiViewModelFactory(repository)
    }

    /** current room ViewModel */
    private val viewModelCurrent: ViewModelCurrent by viewModels {
        CurrentViewModelFactory((requireActivity().applicationContext as BaseApplication).currentRepository)
    }

    /** forecast room ViewModel */
    private val viewModelForecast: ViewModelForecast by viewModels {
        ForecastViewModelFactory((requireActivity().applicationContext as BaseApplication).forecastRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setOnClickListeners()

        loadFromRoom()

        checkConnectivityStatus()

        return binding.root
    }

    private fun loadFromRoom() {
        try {
            loadCurrentFromRoom()
            loadForecastFromRoom()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Network status
     * If no network load from ROOM */
    private fun checkConnectivityStatus() {
        try {
            when {
                ReusableUtils.isDeviceConnected(requireContext()) -> {
                    getCurrentWeather()
                    getForecastWeather()
                }
                else -> {
                    Toast.makeText(requireContext(), "You're offline!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadCurrentFromRoom() {
        try {
            viewModelCurrent.getAllCurrentWeather.observe(viewLifecycleOwner) { current ->
                current.let {
                    Log.d("loadCurrentFromRoom", "$it")
                    if (it != null) {
                        displayCurrentToUI(it)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadForecastFromRoom() {
        try {
            viewModelForecast.getAllForecastWeather.observe(viewLifecycleOwner) { forecast ->
                forecast.let {
                    Log.d("loadForecastFromRoom size", "${it.size}")
                    Log.d("loadForecastFromRoom", "$it")
                    if (it != null) {
                        displayForecastToUI(it)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCurrentWeather() {
        try {
            val params: MutableMap<String, String> = HashMap()
            params["lat"] = "-1.2240624585163389"
            params["lon"] = "36.919931302314055"
            params["appid"] = Constants.APP_ID

            viewModel.fetchCurrentWeather(params)
            viewModel.fetchCurrentWeatherResponse.observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {
                    Log.d(TAG, "${response.body()}")
                    // Delete previous
                    viewModelCurrent.deleteCurrentDetails()
                    insertCurrentToRoom(response.body())
                } else {
                    Toast.makeText(
                        requireContext(),
                        "${response.body()?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Insert Current Weather */
    private fun insertCurrentToRoom(body: CurrentResponseModel?) {
        try {
            viewModelCurrent.insert(
                CurrentWeatherModel(
                    null,
                    body?.name,
                    body?.userCoordinates?.latitude,
                    body?.userCoordinates?.longitude,
                    body?.mainDetails?.temp_current,
                    body?.mainDetails?.temp_min,
                    body?.mainDetails?.temp_max,
                    body?.weatherDetails?.get(0)?.main,
                    body?.weatherDetails?.get(0)?.description
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getForecastWeather() {
        try {
            val params: MutableMap<String, String> = HashMap()
            params["lat"] = "-1.2240624585163389"
            params["lon"] = "36.919931302314055"
            params["appid"] = Constants.APP_ID

            viewModel.fetchForecastWeather(params)
            viewModel.fetchForecastWeatherResponse.observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {
                    Log.d(TAG, "response ${response.body()?.foreCastList?.size} : ${response.body()}")
                    // Delete previous
                    viewModelForecast.deleteForecastDetails()
                    insertForecastToRoom(response.body())
                } else {
                    Toast.makeText(
                        requireContext(),
                        "${response.body()?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Insert forecast Weather */
    private fun insertForecastToRoom(body: ForecastResponseModel?) {
        try {
            for (items in body?.foreCastList?.toMutableList()!!) {
                val forecastWeatherModel = ForecastWeatherModel(
                    null,
                    body.city?.name,
                    body.city?.userCoordinates?.latitude,
                    body.city?.userCoordinates?.longitude,
                    items.mainDetails?.temp_current,
                    items.mainDetails?.temp_min,
                    items.mainDetails?.temp_max,
                    items.weatherDetails?.get(0)?.main,
                    items.weatherDetails?.get(0)?.description,
                    items.dt_txt
                )
                viewModelForecast.insert(forecastWeatherModel)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Display Current weather to UI */
    private fun displayCurrentToUI(it: CurrentWeatherModel) {
        try {
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Display Forecast weather to UI */
    private fun displayForecastToUI(it: List<ForecastWeatherModel>) {
        try {
            when {
                it.toMutableList().isNotEmpty() -> {
                    // TODO recyclerView & Adapter
                }
                else -> {
                    getForecastWeather()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setOnClickListeners() {
        try {
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}