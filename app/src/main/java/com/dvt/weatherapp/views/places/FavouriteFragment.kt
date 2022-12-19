package com.dvt.weatherapp.views.places

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dvt.weatherapp.BaseApplication
import com.dvt.weatherapp.R
import com.dvt.weatherapp.databinding.FragmentFavouritesBinding
import com.dvt.weatherapp.room.entities.FavouriteWeatherModel
import com.dvt.weatherapp.utils.Constants
import com.dvt.weatherapp.utils.ReusableUtils
import com.dvt.weatherapp.viewmodels.FavouriteViewModelFactory
import com.dvt.weatherapp.viewmodels.ViewModelFavourite
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class FavouriteFragment : Fragment(), OnMapReadyCallback, LocationListener {

    private val TAG = "FavouriteFragment"

    private lateinit var binding: FragmentFavouritesBinding

    /** favourite room ViewModel */
    private val viewModelFavourite: ViewModelFavourite by viewModels {
        FavouriteViewModelFactory((requireActivity().applicationContext as BaseApplication).favouriteRepository)
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var mMap: GoogleMap? = null
    private val markerOptions = MarkerOptions()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        // initialize fused client
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        setOnClickListeners()

        return binding.root
    }

    private fun setOnClickListeners() {
        try {
            binding.backFab.setOnClickListener { }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        try {
            mMap = p0

            getCurrentKnownLocation()

            viewModelFavourite.getAllFavouriteWeather.observe(viewLifecycleOwner) { favourites ->
                favourites.let {
                    Log.d("loadForecastFromRoom size", "${it.size}")
                    Log.d("loadForecastFromRoom", "$it")
                    if (it != null && it.toMutableList().isNotEmpty()) {
                        populateFavouriteMarkers(it)
                    }
                }
            }

            // Zoom
            mMap!!.uiSettings.isZoomControlsEnabled = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun populateFavouriteMarkers(it: List<FavouriteWeatherModel>) {
        try {
            for (i in it.toMutableList()) {
                mMap!!.addMarker(
                    markerOptions.position(
                        LatLng(
                            i.latitude!!,
                            i.longitude!!
                        )
                    ).title(
                        "${i.locationName} - ${i.weatherMain} ${
                            ReusableUtils.convertKelvinToCelsius(i.temperature ?: 0.0)
                        }℃"
                    ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .draggable(true) // reposition a marker once its been added to the map
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

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

                    mMap!!.addMarker(
                        markerOptions.position(
                            LatLng(
                                location.latitude,
                                location.longitude
                            )
                        ).title("Your Location").icon(
                            ReusableUtils.bitmapFromVector(
                                requireContext(),
                                R.drawable.baseline_my_location_black_36dp
                            )
                        ).draggable(true) // reposition a marker once its been added to the map
                    )

                    // Focus
                    addCameraToMap(
                        LatLng(
                            location.latitude,
                            location.longitude
                        )
                    )
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

    private fun addCameraToMap(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(11f)
            .build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onLocationChanged(p0: Location) {
        try {
            Log.d(TAG, " locationChanged LAT: ${p0.latitude}, LON: ${p0.longitude}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}