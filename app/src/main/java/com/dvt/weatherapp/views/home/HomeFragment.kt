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
import com.dvt.weatherapp.repository.ApiCallRepository
import com.dvt.weatherapp.room.entities.CurrentWeatherModel
import com.dvt.weatherapp.utils.Constants
import com.dvt.weatherapp.utils.ReusableUtils
import com.dvt.weatherapp.viewmodels.ApiViewModel
import com.dvt.weatherapp.viewmodels.ApiViewModelFactory
import com.dvt.weatherapp.viewmodels.CurrentViewModel
import com.dvt.weatherapp.viewmodels.CurrentViewModelFactory

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private lateinit var binding : FragmentHomeBinding

    private val viewModel: ApiViewModel by viewModels {
        val repository = ApiCallRepository()
        ApiViewModelFactory(repository)
    }

    /** current room ViewModel */
    private val currentViewModel: CurrentViewModel by viewModels {
        CurrentViewModelFactory((requireActivity().applicationContext as BaseApplication).currentRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setOnClickListeners()

        checkConnectivityStatus()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCurrentFromRoom()
        loadForecastFromRoom()
    }

    /** Network status
     * If no network load from ROOM */
    private fun checkConnectivityStatus() {
        try {
            if (ReusableUtils.isDeviceConnected(requireContext())) {
                getCurrentWeather()
                getForecastWeather()
            } else {
                loadCurrentFromRoom()
                loadForecastFromRoom()
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    private fun loadCurrentFromRoom() {
        try {
            currentViewModel.getAllCurrentWeather.observe(viewLifecycleOwner) { current ->
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

    /**
     * Display Current weather to UI
     */
    private fun displayCurrentToUI(it: CurrentWeatherModel) {
        try {
            // TODO Display
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadForecastFromRoom() {
        try {
            // TODO room logic and if empty pull from server as well
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
                    currentViewModel.deleteCurrentDetails()
                    insertCurrentToRoom(response.body())
                } else {
                    Toast.makeText(requireContext(), "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Insert Current Weather */
    private fun insertCurrentToRoom(body: CurrentResponseModel?) {
        try {
            currentViewModel.insert(
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
                    Log.d(TAG, "${response.body()}")
                } else {
                    Toast.makeText(requireContext(), "${response.body()?.message}", Toast.LENGTH_SHORT).show()
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