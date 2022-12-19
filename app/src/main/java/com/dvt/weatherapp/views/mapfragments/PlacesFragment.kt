package com.dvt.weatherapp.views.mapfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dvt.weatherapp.databinding.FragmentPlacesBinding

class PlacesFragment : Fragment() {

    private val TAG = "DetailsFragment"

    private lateinit var binding: FragmentPlacesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPlacesBinding.inflate(inflater, container, false)

        return binding.root
    }
}