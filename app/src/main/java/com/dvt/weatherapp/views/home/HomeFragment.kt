package com.dvt.weatherapp.views.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dvt.weatherapp.databinding.FragmentHomeBinding
import com.dvt.weatherapp.repository.ApiCallRepository
import com.dvt.weatherapp.utils.Constants
import com.dvt.weatherapp.utils.ReusableUtils
import com.dvt.weatherapp.viewmodels.ApiViewModel
import com.dvt.weatherapp.viewmodels.ApiViewModelFactory

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private lateinit var binding : FragmentHomeBinding

    private val viewModel: ApiViewModel by viewModels {
        val repository = ApiCallRepository()
        ApiViewModelFactory(repository)
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

    private fun checkConnectivityStatus() {
        try {
            if (ReusableUtils.isDeviceConnected(requireContext())) {
                getCurrentWeather()
            } else {
                loadFromRoom()
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    private fun loadFromRoom() {
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
                    getForecastWeather()
                } else {
                    Toast.makeText(requireContext(), "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            }
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