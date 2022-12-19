package com.dvt.weatherapp.entitymodel

import com.dvt.weatherapp.room.entities.CurrentWeatherModel
import com.dvt.weatherapp.room.entities.FavouriteWeatherModel
import com.dvt.weatherapp.room.entities.ForecastWeatherModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class TestEntityModel {

    private lateinit var modelCurrent: CurrentWeatherModel
    private lateinit var modelForecast: ForecastWeatherModel
    private lateinit var modelFavourite: FavouriteWeatherModel

    @Before
    fun setUp() {
        modelCurrent = CurrentWeatherModel(
            1,
            "Nairobi West",
            -1.2240624585163389,
            36.919931302314055,
            125.64,
            125.32,
            134.43,
            "Sunny",
            "Clear sky"
        )

        modelForecast = ForecastWeatherModel(
            2,
            "Westlands, Nairobi",
            -1.2240624585163389,
            36.919931302314055,
            125.64,
            125.32,
            134.43,
            "Rainy",
            "Hailstorms",
            "2022-12-19 22:38:00",
            "Yes"
        )

        modelFavourite = FavouriteWeatherModel(
            3,
            2,
            "Nairobi West",
            -1.2240624585163389,
            36.919931302314055,
            125.64,
            125.32,
            134.43,
            "Sunny",
            "Clear sky",
            "2022-12-19 22:38:00"
        )
    }

    @Test
    fun getCurrentEntityModel() {
        Assert.assertEquals(
            CurrentWeatherModel(
                1,
                "Nairobi West",
                -1.2240624585163389,
                36.919931302314055,
                125.64,
                125.32,
                134.43,
                "Sunny",
                "Clear sky"
            ), modelCurrent
        )
    }

    @Test
    fun getForecastEntityModel() {
        Assert.assertEquals(
            ForecastWeatherModel(
                2,
                "Westlands, Nairobi",
                -1.2240624585163389,
                36.919931302314055,
                125.64,
                125.32,
                134.43,
                "Rainy",
                "Hailstorms",
                "2022-12-19 22:38:00",
                "Yes"
            ), modelForecast
        )
    }

    @Test
    fun getFavouriteEntityModel() {
        Assert.assertEquals(
            FavouriteWeatherModel(
                3,
                2,
                "Nairobi West",
                -1.2240624585163389,
                36.919931302314055,
                125.64,
                125.32,
                134.43,
                "Sunny",
                "Clear sky",
                "2022-12-19 22:38:00"
            ), modelFavourite
        )
    }
}