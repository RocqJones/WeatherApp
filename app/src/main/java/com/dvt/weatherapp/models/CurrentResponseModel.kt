package com.dvt.weatherapp.models

import com.google.gson.annotations.SerializedName

/** current weather data*/
data class CurrentResponseModel(
    @SerializedName("cod") val code: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("id") val id: Long? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("coord") val userCoordinates: UserCoordinates? = null,
    @SerializedName("weather") val weatherDetails: List<WeatherDetails>? = null,
    @SerializedName("main") val mainDetails: MainDetails? = null
)

data class UserCoordinates(
    @SerializedName("lat") val latitude: Double? = null,
    @SerializedName("lon") val longitude: Double? = null
)

data class WeatherDetails(
    @SerializedName("main") val main: String? = null,
    @SerializedName("description") val description: String? = null
)

data class MainDetails(
    @SerializedName("temp") val temp_current: Double? = null,
    @SerializedName("feels_like") val feels_like: Double? = null,
    @SerializedName("temp_min") val temp_min: Double? = null,
    @SerializedName("temp_max") val temp_max: Double? = null
)

