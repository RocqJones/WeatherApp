package com.dvt.weatherapp.views.mapfragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dvt.weatherapp.BaseApplication
import com.dvt.weatherapp.R
import com.dvt.weatherapp.adapters.AdapterForecast
import com.dvt.weatherapp.databinding.FragmentDetailsBinding
import com.dvt.weatherapp.listeners.ForecastWeatherListener
import com.dvt.weatherapp.models.CurrentResponseModel
import com.dvt.weatherapp.models.ForecastResponseModel
import com.dvt.weatherapp.repository.ApiCallRepository
import com.dvt.weatherapp.room.entities.FavouriteWeatherModel
import com.dvt.weatherapp.room.entities.ForecastWeatherModel
import com.dvt.weatherapp.utils.Constants
import com.dvt.weatherapp.utils.DataFactory
import com.dvt.weatherapp.utils.ReusableUtils
import com.dvt.weatherapp.viewmodels.*

class DetailsFragment : Fragment() {

    private val TAG = "DetailsFragment"

    private lateinit var binding: FragmentDetailsBinding

    private val viewModel: ApiViewModel by viewModels {
        val repository = ApiCallRepository()
        ApiViewModelFactory(repository)
    }

    /** favourite room ViewModel */
    private val viewModelFavourite: ViewModelFavourite by viewModels {
        FavouriteViewModelFactory((requireActivity().applicationContext as BaseApplication).favouriteRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        updateUI()

        setOnClickListeners()

        return binding.root
    }

    private fun updateUI() {
        try {
            binding.tvPlaceName.text = DataFactory.getPlaceModel()?.placeName ?: ""
            checkConnectivityStatus(
                DataFactory.getPlaceModel()?.placeLatLong?.latitude.toString(),
                DataFactory.getPlaceModel()?.placeLatLong?.longitude.toString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setOnClickListeners() {
        try {
            binding.backFab.setOnClickListener { requireActivity().onBackPressed() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Network status
     * If no network load from ROOM */
    private fun checkConnectivityStatus(lat: String, lon: String) {
        try {
            when {
                ReusableUtils.isDeviceConnected(requireContext()) -> {
                    getCurrentWeather(lat, lon)
                    getForecastWeather(lat, lon)
                }
                else -> {
                    Toast.makeText(requireContext(), "You're offline, turn on network!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCurrentWeather(lat: String, lon: String) {
        try {
            val params: MutableMap<String, String> = HashMap()
            params["lat"] = lat
            params["lon"] = lon
            params["appid"] = Constants.APP_ID

            viewModel.fetchCurrentWeather(params)
            viewModel.fetchCurrentWeatherResponse.observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {
                    Log.d(TAG, "${response.body()}")
                    displayCurrent(response.body())
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

    @SuppressLint("SetTextI18n")
    private fun displayCurrent(body: CurrentResponseModel?) {
        try {
            binding.tvDegree.text = "${ReusableUtils.convertKelvinToCelsius(body?.mainDetails?.temp_current ?: 0.0)}℃"
            binding.tvMinTemp.text = "${ReusableUtils.convertKelvinToCelsius(body?.mainDetails?.temp_min ?: 0.0)}℃"
            binding.tvCurrentTemp.text = "${ReusableUtils.convertKelvinToCelsius(body?.mainDetails?.temp_current ?: 0.0)}℃"
            binding.tvMaxTemp.text = "${ReusableUtils.convertKelvinToCelsius(body?.mainDetails?.temp_max ?: 0.0)}℃"
            binding.tvDescription.text = body?.weatherDetails?.get(0)?.description ?: ""

            when {
                body?.weatherDetails?.get(0)?.main.equals("Clouds") -> {
                    cloudBg()
                }
                body?.weatherDetails?.get(0)?.main.equals("Rain") -> {
                    rainBb()
                }
                else -> {
                    sunnyBg()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun cloudBg() {
        try {
            binding.linearLayout0.background = ContextCompat.getDrawable(requireContext(), R.drawable.forest_cloudy)
            binding.linearLayout1.background = ContextCompat.getDrawable(requireContext(), R.drawable.forest_cloudy)
            binding.linearLayout2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_cloudy))
            binding.rvForecast.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_cloudy))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun rainBb() {
        try {
            binding.linearLayout0.background = ContextCompat.getDrawable(requireContext(), R.drawable.forest_rainy)
            binding.linearLayout1.background = ContextCompat.getDrawable(requireContext(), R.drawable.forest_rainy)
            binding.linearLayout2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_rainy))
            binding.rvForecast.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_rainy))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sunnyBg() {
        try {
            binding.linearLayout0.background = ContextCompat.getDrawable(requireContext(), R.drawable.forest_sunny)
            binding.linearLayout1.background = ContextCompat.getDrawable(requireContext(), R.drawable.forest_sunny)
            binding.linearLayout2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_sunny))
            binding.rvForecast.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_sunny))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getForecastWeather(lat: String, lon: String) {
        try {
            val params: MutableMap<String, String> = HashMap()
            params["lat"] = lat
            params["lon"] = lon
            params["appid"] = Constants.APP_ID

            viewModel.fetchForecastWeather(params)
            viewModel.fetchForecastWeatherResponse.observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {
                    Log.d(TAG, "response ${response.body()?.foreCastList?.size} : ${response.body()}")
                    displayToRecyclerView(response.body())
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

    private fun displayToRecyclerView(body: ForecastResponseModel?) {
        try {
            val emptyArrayList : ArrayList<ForecastWeatherModel> = ArrayList()
            for (items in body?.foreCastList?.toMutableList()!!) {
                emptyArrayList.add(
                    ForecastWeatherModel(
                        null,
                        body.city?.name,
                        body.city?.userCoordinates?.latitude,
                        body.city?.userCoordinates?.longitude,
                        items.mainDetails?.temp_current,
                        items.mainDetails?.temp_min,
                        items.mainDetails?.temp_max,
                        items.weatherDetails?.get(0)?.main,
                        items.weatherDetails?.get(0)?.description,
                        items.dt_txt,
                        "No"
                    )
                )
            }

            binding.rvForecast.layoutManager = LinearLayoutManager(context)
            binding.rvForecast.adapter = AdapterForecast(
                emptyArrayList, requireContext(),
                object : ForecastWeatherListener {
                    override fun onResponse(model: ForecastWeatherModel, i: Int) {
                        try {
                            when (i) {
                                201 -> {
                                    updateAndAddFavourite(model)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateAndAddFavourite(model: ForecastWeatherModel) {
        try {
            // Add new favourite item to room
            viewModelFavourite.insert(
                FavouriteWeatherModel(
                    null,
                    model.id,
                    model.locationName,
                    model.latitude,
                    model.longitude,
                    model.temperature,
                    model.temp_min,
                    model.temp_max,
                    model.weatherMain,
                    model.weatherDescription,
                    model.forecastDate
                )
            )
            Toast.makeText(requireContext(), "Successfully added favourite!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}