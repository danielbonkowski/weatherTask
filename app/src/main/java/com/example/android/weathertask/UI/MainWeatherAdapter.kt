package com.example.android.weathertask.UI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.weathertask.Data.WeatherForecast
import com.example.android.weathertask.R
import com.example.android.weathertask.Utils

class MainWeatherAdapter (private val dataSet: Array<WeatherForecast>,
                          private val mMainForecastClickHandler: MainWeatherAdapterOnClickHandler
) :
    RecyclerView.Adapter<MainWeatherAdapter.WeatherViewHolder>() {



    interface MainWeatherAdapterOnClickHandler{
        fun onClick(weatherForecast: WeatherForecast)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_forecast_item, parent, false);
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holderWeather: WeatherViewHolder, position: Int) {
        val highestTemp = Utils.findHighestTempForSingleCity(dataSet[position])
        val formattedHighestTemp =
            Utils.formatTemperature(holderWeather.textViewWeather.context, highestTemp)
        val tempData : String = dataSet[position].city + ": " + formattedHighestTemp
        holderWeather.textViewWeather.text = tempData
        holderWeather.imageViewWeather.setImageResource(R.drawable.ic_launcher_background)
    }

    override fun getItemCount() = dataSet.size

    inner class WeatherViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val textViewWeather = itemView.findViewById(R.id.main_weather_text_view) as TextView
        val imageViewWeather = itemView.findViewById(R.id.main_weather_image_view) as ImageView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val adapterPosition = adapterPosition
            val weatherForcast = dataSet[adapterPosition]
            mMainForecastClickHandler.onClick(weatherForcast)
        }
    }
}