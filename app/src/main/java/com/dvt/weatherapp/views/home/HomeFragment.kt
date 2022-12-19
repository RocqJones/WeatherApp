package com.dvt.weatherapp.views.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dvt.weatherapp.BaseApplication
import com.dvt.weatherapp.R
import com.dvt.weatherapp.adapters.AdapterForecast
import com.dvt.weatherapp.databinding.FragmentHomeBinding
import com.dvt.weatherapp.listeners.ForecastWeatherListener
import com.dvt.weatherapp.models.CurrentResponseModel
import com.dvt.weatherapp.models.ForecastResponseModel
import com.dvt.weatherapp.repository.ApiCallRepository
import com.dvt.weatherapp.room.entities.CurrentWeatherModel
import com.dvt.weatherapp.room.entities.FavouriteWeatherModel
import com.dvt.weatherapp.room.entities.ForecastWeatherModel
import com.dvt.weatherapp.utils.Constants
import com.dvt.weatherapp.utils.ReusableUtils
import com.dvt.weatherapp.viewmodels.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class HomeFragment : Fragment(), LocationListener {

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

    /** favourite room ViewModel */
    private val viewModelFavourite: ViewModelFavourite by viewModels {
        FavouriteViewModelFactory((requireActivity().applicationContext as BaseApplication).favouriteRepository)
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val locationRequestCode = 99

    private var firstVisit = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setOnClickListeners()

        loadFromRoom()

        firstVisit = true

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initialize fused client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // location permissions
        checkLocationPermission()
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
    private fun checkConnectivityStatus(lat: String, lon: String) {
        try {
            when {
                ReusableUtils.isDeviceConnected(requireContext()) -> {
                    getCurrentWeather(lat, lon)
                    getForecastWeather(lat, lon)
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
                    items.dt_txt,
                    "No"
                )
                viewModelForecast.insert(forecastWeatherModel)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Display Current weather to UI */
    @SuppressLint("SetTextI18n")
    private fun displayCurrentToUI(it: List<CurrentWeatherModel>) {
        try {
            if (it.toMutableList().isNotEmpty()) {
                binding.tvDegree.text = "${ReusableUtils.convertKelvinToCelsius(it[0].temperature ?: 0.0)}℃"
                binding.tvMinTemp.text = "${ReusableUtils.convertKelvinToCelsius(it[0].temp_min ?: 0.0)}℃"
                binding.tvCurrentTemp.text = "${ReusableUtils.convertKelvinToCelsius(it[0].temperature ?: 0.0)}℃"
                binding.tvMaxTemp.text = "${ReusableUtils.convertKelvinToCelsius(it[0].temp_max ?: 0.0)}℃"
                binding.tvDescription.text = it[0].weatherMain

                when {
                    it[0].weatherMain.equals("Clouds") -> {
                        cloudBg()
                    }
                    it[0].weatherMain.equals("Rain") -> {
                        rainBb()
                    }
                    else -> {
                        sunnyBg()
                    }
                }
            } else {
                cloudBg()
                getCurrentKnownLocation()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun cloudBg() {
        try {
            binding.linearLayout1.background = ContextCompat.getDrawable(requireContext(), R.drawable.forest_cloudy)
            binding.linearLayout2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_cloudy))
            binding.rvForecast.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_cloudy))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun rainBb() {
        try {
            binding.linearLayout1.background = ContextCompat.getDrawable(requireContext(), R.drawable.forest_rainy)
            binding.linearLayout2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_rainy))
            binding.rvForecast.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_rainy))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sunnyBg() {
        try {
            binding.linearLayout1.background = ContextCompat.getDrawable(requireContext(), R.drawable.forest_sunny)
            binding.linearLayout2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_sunny))
            binding.rvForecast.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_sunny))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Display Forecast weather to UI */
    private fun displayForecastToUI(it: List<ForecastWeatherModel>) {
        try {
            when {
                it.toMutableList().isNotEmpty() -> {
                    binding.rvForecast.layoutManager = LinearLayoutManager(context)
                    binding.rvForecast.adapter = AdapterForecast(
                        it, requireContext(),
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
                }
                else -> {
                    getCurrentKnownLocation()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateAndAddFavourite(model: ForecastWeatherModel) {
        try {
            viewModelForecast.update(model.id!!, "Yes")

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

    private fun setOnClickListeners() {
        try {
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onLocationChanged(p0: Location) {
        try {
            Log.d("locationChanged", "LON: ${p0.longitude}, LAT: ${p0.latitude}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkLocationPermission() {
        try {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        locationRequestCode
                    )
                } else {
                    // We can request the permission.
                    ActivityCompat.requestPermissions(
                        requireActivity(), arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        locationRequestCode
                    )
                }
            } else {
                // Permission previously granted
                getCurrentKnownLocation()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** We ignore this permission complain because we've already handled it before getting here */
    @SuppressLint("MissingPermission")
    private fun getCurrentKnownLocation() {
        try {
            fusedLocationProviderClient.getCurrentLocation(
                Constants.priority,
                Constants.cancellationTokenSource.token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    // Logic to handle location object
                    Log.d("getCurrentLocation", "${location.latitude}, ${location.longitude}")
                    checkConnectivityStatus(location.latitude.toString(), location.longitude.toString())
                } else {
                    Log.d("getCurrentLocation", "Returned Null")
                    ReusableUtils.checkGPSifEnabled(requireActivity())
                }
            }.addOnFailureListener { exception ->
                Log.d("getCurrentLocation", "location failed with exception: $exception")
                // is location - GPS services enabled
                ReusableUtils.checkGPSifEnabled(requireActivity())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationRequestCode)
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                // permission was granted, do location-related task you need to do.
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    getCurrentKnownLocation()
                }
            } else {
                // permission denied! Disable the functionality that depends on this permission.
                ReusableUtils.normalDialog(
                    "Permission denied!",
                    getString(R.string.location_permission_denied),
                    requireContext()
                )
            }
    }

    override fun onResume() {
        super.onResume()
        if (!firstVisit) {
            // do this for second visit only
            Log.d(TAG, "triggeredAgain 2")
            getCurrentKnownLocation()
        } else {
            Log.d(TAG, "triggeredAgain 1")
        }
    }
}