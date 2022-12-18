package com.dvt.weatherapp.models

import com.google.gson.annotations.SerializedName

/** 5 day weather forecast */
data class ForecastResponseModel(
    @SerializedName("cod") val code: Int? = null,
    @SerializedName("message") val message: Int? = null,
    @SerializedName("city") val city: City? = null,
    @SerializedName("list") val foreCastList: List<ForeCastList>? = null,
)

data class ForeCastList(
    @SerializedName("dt") val dt: Long? = null,
    @SerializedName("main") val mainDetails: MainDetails? = null,
    @SerializedName("weather") val weatherDetails: List<WeatherDetails>? = null,
    @SerializedName("dt_txt") val dt_txt: String? = null
)

data class City(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("coord") val userCoordinates: UserCoordinates? = null,
)
