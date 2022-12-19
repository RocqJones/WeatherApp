package com.dvt.weatherapp.testresponse

import com.dvt.weatherapp.models.CurrentResponseModel
import com.dvt.weatherapp.models.MainDetails
import com.dvt.weatherapp.models.UserCoordinates
import com.dvt.weatherapp.models.WeatherDetails
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class TestCurrentResponseModel {

    private lateinit var response: CurrentResponseModel

    @Before
    fun setUp() {
        response = CurrentResponseModel(
            200,
            "Success",
            3122,
            "Nairobi",
            userCoordinates = UserCoordinates(
                -1.2240624585163389,
                36.919931302314055
            ),
            weatherDetails = listOf(
                WeatherDetails(
                    "Sunny",
                    "Clear sky"
                )
            ),
            mainDetails = MainDetails(
                292.6,
                292.38,
                291.62,
                292.78
            )
        )
    }

    @Test
    fun getCoordinates() {
        Assert.assertEquals(
            UserCoordinates(
                -1.2240624585163389,
                36.919931302314055
            ), response.userCoordinates
        )
    }

    @Test
    fun getTemperatureDetails() {
        Assert.assertEquals(
            MainDetails(
                292.6,
                292.38,
                291.62,
                292.78
            ), response.mainDetails
        )
    }

    @Test
    fun getWeatherDetails() {
        Assert.assertEquals(
            "Clear sky", response.weatherDetails?.get(0)?.description
        )
    }
}