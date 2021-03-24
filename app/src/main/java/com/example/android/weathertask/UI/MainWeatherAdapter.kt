package com.example.android.weathertask.UI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.weathertask.Data.WeatherForecast
import com.example.android.weathertask.R
import com.example.android.weathertask.Utils.Utils

class MainWeatherAdapter( private var dataSet: Array<WeatherForecast>,
                         private val mMainForecastClickHandler: MainWeatherAdapterOnClickHandler
) :
    RecyclerView.Adapter<MainWeatherAdapter.WeatherViewHolder>() {

    private val VIEW_TYPE_MAIN : Int = 0
    private val VIEW_TYPE_LIST : Int = 1
    private val NR_OF_MAIN_ITEMS = 1

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
            .inflate(layoutId, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holderWeather: WeatherViewHolder, position: Int) {

        when(getItemViewType(position)){
            VIEW_TYPE_MAIN -> {
                bindColdestCity(holderWeather)
                bindLowestTemp(holderWeather)
            }
            else -> {
                bindCity(position, holderWeather)
                bindWeatherIcon(position, holderWeather)
                bindWeatherDescription(position, holderWeather)
                bindHighestTemp(position, holderWeather)
            }
        }
    }

    private fun bindColdestCity(holderWeather: WeatherViewHolder) {
        holderWeather.coldestCity.text = Utils.findCityWithLowestAverageDailyTemperature(dataSet)
    }

    private fun bindLowestTemp(holderWeather: WeatherViewHolder) {
        val lowestTemp = Utils.findLowestTempInAllCities(dataSet)
        holderWeather.lowestTemp.text =
            Utils.formatTemperature(holderWeather.lowestTemp.context, lowestTemp)
    }

    private fun bindCity(position: Int, holderWeather: WeatherViewHolder) {
        val tempData: String = dataSet[position - NR_OF_MAIN_ITEMS].city + ": "
        holderWeather.city.text = tempData
    }

    private fun bindWeatherIcon(position: Int, holderWeather: WeatherViewHolder) {
        val weatherIcon = Utils.getWeatherIcon(dataSet[position - NR_OF_MAIN_ITEMS])
        holderWeather.weatherIcon.setImageResource(weatherIcon)
    }

    private fun bindWeatherDescription(position: Int, holderWeather: WeatherViewHolder) {
        val weatherDescription = Utils.getWeatherDescription(dataSet[position - NR_OF_MAIN_ITEMS])
        holderWeather.weatherDescription.text = weatherDescription
    }

    private fun bindHighestTemp(position: Int, holderWeather: WeatherViewHolder) {
        val highestTemp = Utils.findHighestTempForSingleCity(dataSet[position - NR_OF_MAIN_ITEMS])
        val formattedHighestTemp =
            Utils.formatTemperature(holderWeather.city.context, highestTemp)
        holderWeather.maxTemp.text = formattedHighestTemp
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> VIEW_TYPE_MAIN
            else -> VIEW_TYPE_LIST
        }
    }

    override fun getItemCount(): Int {
        return if(dataSet.isNotEmpty()) dataSet.size + NR_OF_MAIN_ITEMS else 0
    }

    inner class WeatherViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val city = itemView.findViewById<TextView>(R.id.city_text_view)
        val weatherIcon = itemView.findViewById<ImageView>(R.id.weather_icon)
        val weatherDescription = itemView.findViewById<TextView>(R.id.weather_description_text_view)
        val maxTemp = itemView.findViewById<TextView>(R.id.max_temp_text_view)
        val coldestCity = itemView.findViewById<TextView>(R.id.coldest_city_text_view)
        val lowestTemp = itemView.findViewById<TextView>(R.id.lowest_temp_text_view)

        init {
            if(itemView.findViewById<TextView>(R.id.max_temp_text_view) != null){
                itemView.setOnClickListener(this)
            }
        }

        override fun onClick(v: View?) {
            val adapterPosition = adapterPosition
            val weatherForecast = dataSet[adapterPosition - NR_OF_MAIN_ITEMS]
            if(v != null){
                mMainForecastClickHandler.onClick(weatherForecast, v)
            }
        }
    }
}