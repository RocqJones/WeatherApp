package com.dvt.weatherapp.utils

import com.google.android.gms.maps.model.LatLng

object DataFactory {
    private var placesModel: PlacesModel? = null

    fun setPlaceModel(model: PlacesModel) {
        placesModel = model
    }

    fun getPlaceModel(): PlacesModel? {
        if (placesModel == null) {
            placesModel = null
        }
        return placesModel
    }
}

data class PlacesModel (
    var _id: String? = null,
    var placeName: String? = null,
    var placeLatLong: LatLng? = null
)