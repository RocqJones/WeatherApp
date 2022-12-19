package com.dvt.weatherapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dvt.weatherapp.R
import com.dvt.weatherapp.databinding.ItemForecastBinding
import com.dvt.weatherapp.listeners.ForecastWeatherListener
import com.dvt.weatherapp.room.entities.ForecastWeatherModel
import com.dvt.weatherapp.utils.ReusableUtils

class AdapterForecast (
    var forecastListModel: List<ForecastWeatherModel>,
    var mContext: Context,
    private val listener: ForecastWeatherListener
    ) : RecyclerView.Adapter<AdapterForecast.RVHolder>() {

    private lateinit var binding: ItemForecastBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        binding = ItemForecastBinding.inflate(LayoutInflater.from(parent.context))
        return RVHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        val binding = holder.binding

        binding.tvDayOfWeek.text = forecastListModel[position].forecastDate

        binding.tvTemperature.text = "${ReusableUtils.convertKelvinToCelsius(
            forecastListModel[position].temperature ?: 0.0
        )} ℃"

        when {
            forecastListModel[position].weatherMain.equals("Clouds") -> {
                binding.imgConditionIcon.background = ContextCompat.getDrawable(mContext, R.drawable.partlysunny_3x)
            }
            forecastListModel[position].weatherMain.equals("Rain") -> {
                binding.imgConditionIcon.background = ContextCompat.getDrawable(mContext, R.drawable.rain_3x)
            }
            else -> {
                binding.imgConditionIcon.background = ContextCompat.getDrawable(mContext, R.drawable.clear_3x)
            }
        }

        when {
            forecastListModel[position].liked.equals("Yes") -> {
                binding.addFavourite.background = ContextCompat.getDrawable(mContext, R.drawable.ic_baseline_favorite_red_20)
            }
            else -> {
                binding.addFavourite.background = ContextCompat.getDrawable(mContext, R.drawable.ic_baseline_favorite_20)
            }
        }

        // Flag this item as favourite
        binding.addFavourite.setOnClickListener {
            Handler(Looper.getMainLooper()).post { listener.onResponse(forecastListModel[position], 201) }
        }
    }

    override fun getItemCount(): Int {
        return forecastListModel.size
    }

    class RVHolder(val binding: ItemForecastBinding) : RecyclerView.ViewHolder(binding.root)
    /*
    class RVHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var addFavourite : ImageView
        var tvDayOfWeek: TextView
        var imgConditionIcon : ImageView
        var tvTemperature: TextView

        init {
            val itemForecastBinding: ItemForecastBinding = ItemForecastBinding.bind(itemView)
            addFavourite = itemForecastBinding.addFavourite
            tvDayOfWeek = itemForecastBinding.tvDayOfWeek
            tvTemperature = itemForecastBinding.tvTemperature
            imgConditionIcon = itemForecastBinding.imgConditionIcon
        }
    }
    */
}