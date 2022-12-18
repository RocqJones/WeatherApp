package com.dvt.weatherapp.network

import com.dvt.weatherapp.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WebService {
    /** Build web service  */
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /** create instance of API interface  */
    val api: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}