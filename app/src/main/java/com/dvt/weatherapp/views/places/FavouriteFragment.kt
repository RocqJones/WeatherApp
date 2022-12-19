package com.dvt.weatherapp.views.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dvt.weatherapp.BaseApplication
import com.dvt.weatherapp.databinding.FragmentFavouritesBinding
import com.dvt.weatherapp.viewmodels.FavouriteViewModelFactory
import com.dvt.weatherapp.viewmodels.ViewModelFavourite
import com.google.android.gms.location.FusedLocationProviderClient

class FavouriteFragment : Fragment() {

    private val TAG = "FavouriteFragment"

    private lateinit var binding: FragmentFavouritesBinding

    /** favourite room ViewModel */
    private val viewModelFavourite: ViewModelFavourite by viewModels {
        FavouriteViewModelFactory((requireActivity().applicationContext as BaseApplication).favouriteRepository)
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val locationRequestCode = 99

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)



        return binding.root
    }
}