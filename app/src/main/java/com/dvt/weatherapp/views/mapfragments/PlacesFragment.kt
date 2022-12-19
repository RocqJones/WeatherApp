package com.dvt.weatherapp.views.mapfragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.dvt.weatherapp.R
import com.dvt.weatherapp.databinding.FragmentPlacesBinding
import com.dvt.weatherapp.utils.DataFactory
import com.dvt.weatherapp.utils.PlacesModel
import com.dvt.weatherapp.views.MainActivity
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*

class PlacesFragment : Fragment() {

    private val TAG = "PlacesFragment"

    private val autoCompleteRequestCode = 1

    private lateinit var binding: FragmentPlacesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPlacesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            (this.requireContext() as MainActivity).supportFragmentManager
                .findFragmentById(R.id.autocomplete_fragment) as? AutocompleteSupportFragment

        if (!Places.isInitialized()) {
            Places.initialize(this.requireContext(), getString(R.string.api_key), Locale.US)
        }

        // Specify the types of place data to return.
        autocompleteFragment?.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Get info about the selected place.
                Log.i(TAG, " selectedPlaceInfo: ${place.name}, ${place.id}, ${place.latLng}")
            }

            override fun onError(status: Status) {
                // Handle the error.
                Log.i(TAG, "selectedPlaceError: $status")
            }
        })

        // Set the fields to specify which types of place data to return after the user has made a selection.
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this.requireContext())
        startActivityForResult(intent, autoCompleteRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == autoCompleteRequestCode) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i(TAG, "placeResult: ${place.name}, ${place.id}, ${place.latLng}")
                        // placeResult: Nairobi, ChIJp0lN2HIRLxgRTJKXslQCz_c, LatLon: lat/lng: (-1.2920659,36.8219462)

                        DataFactory.setPlaceModel(
                            PlacesModel(
                                place.id,
                                place.name,
                                place.latLng
                            )
                        )

                        NavHostFragment.findNavController(this).navigate(
                            R.id.action_placesFragment_to_detailsFragment
                        )
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i("placesErr", status.statusMessage ?: "")
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}