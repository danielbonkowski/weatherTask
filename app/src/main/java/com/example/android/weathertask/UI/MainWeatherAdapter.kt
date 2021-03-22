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

class MainWeatherAdapter(private var dataSet: Array<WeatherForecast>,
                         private val mMainForecastClickHandler: MainWeatherAdapterOnClickHandler
) :
    RecyclerView.Adapter<MainWeatherAdapter.WeatherViewHolder>() {

    private val VIEW_TYPE_MAIN : Int = 0
    private val VIEW_TYPE_LIST : Int = 1

    interface MainWeatherAdapterOnClickHandler{
        fun onClick(weatherForecast: WeatherForecast, view: View)
    }

    fun setData(newDataSet: Array<WeatherForecast>){
        dataSet = newDataSet
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {

        val layoutId = when(viewType){
            VIEW_TYPE_MAIN -> R.layout.cities_forecast_main_list_item
            else -> R.layout.cities_forecast_list_item
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false);
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holderWeather: WeatherViewHolder, position: Int) {

        when(getItemViewType(position)){
            VIEW_TYPE_MAIN -> {
                holderWeather.coldestCity.text = Utils.findCityWithLowestAverageDailyTemperature(dataSet)
                val lowestTemp = Utils.findLowestTempInAllCities(dataSet)
                holderWeather.lowestTemp.text = Utils.formatTemperature(holderWeather.lowestTemp.context, lowestTemp)
            }
            else -> {
                val highestTemp = Utils.findHighestTempForSingleCity(dataSet[position - 1])
                val formattedHighestTemp =
                    Utils.formatTemperature(holderWeather.city.context, highestTemp)
                val tempData : String = dataSet[position - 1].city + ": "
                holderWeather.city.text = tempData
                val weatherIcon = getWeatherIcon(dataSet[position - 1])
                val weatherDescription = getWeatherDescription(dataSet[position - 1])
                holderWeather.weatherIcon.setImageResource(weatherIcon)
                holderWeather.weatherDescription.text = weatherDescription
                holderWeather.maxTemp.text = formattedHighestTemp
            }
        }

    }

    private fun getWeatherDescription(weatherForecast: WeatherForecast): String {
        return weatherForecast.weather.capitalize()
    }

    private fun getWeatherIcon(weatherForecast: WeatherForecast): Int {
        return when(weatherForecast.weather){
            "cloudy" -> R.drawable.ic_cloudy
            "sunny" -> R.drawable.ic_sunny
            "rainy" -> R.drawable.ic_rainy
            else -> R.drawable.ic_snowy
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> VIEW_TYPE_MAIN
            else -> VIEW_TYPE_LIST
        }
    }

    override fun getItemCount(): Int {
        return if(dataSet.isNotEmpty()) dataSet.size + 1 else 0
    }

    inner class WeatherViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val city = itemView.findViewById<TextView>(R.id.city_text_view)
        val weatherIcon = itemView.findViewById<ImageView>(R.id.weather_icon)
        val weatherDescription = itemView.findViewById<TextView>(R.id.weather_desctiption_text_view)
        val maxTemp = itemView.findViewById<TextView>(R.id.max_temp_text_view)
        val coldestCity = itemView.findViewById<TextView>(R.id.coldest_city_text_view)
        val lowestTemp = itemView.findViewById<TextView>(R.id.lowest_temp_text_view)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val adapterPosition = adapterPosition
            val weatherForcast = dataSet[adapterPosition - 1]
            if(v != null){
                mMainForecastClickHandler.onClick(weatherForcast, v)
            }
        }
    }
}