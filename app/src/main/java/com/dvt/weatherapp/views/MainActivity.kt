package com.dvt.weatherapp.views

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dvt.weatherapp.BaseApplication
import com.dvt.weatherapp.databinding.ActivityMainBinding
import com.dvt.weatherapp.utils.ReusableUtils
import com.dvt.weatherapp.viewmodels.CurrentViewModelFactory
import com.dvt.weatherapp.viewmodels.ForecastViewModelFactory
import com.dvt.weatherapp.viewmodels.ViewModelCurrent
import com.dvt.weatherapp.viewmodels.ViewModelForecast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /** current room ViewModel */
    private val viewModelCurrent: ViewModelCurrent by viewModels {
        CurrentViewModelFactory((this.applicationContext as BaseApplication).currentRepository)
    }

    /** forecast room ViewModel */
    private val viewModelForecast: ViewModelForecast by viewModels {
        ForecastViewModelFactory((this.applicationContext as BaseApplication).forecastRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}