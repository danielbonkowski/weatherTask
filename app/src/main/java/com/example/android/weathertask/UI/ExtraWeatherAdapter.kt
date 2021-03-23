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

class ExtraWeatherAdapter (private var dataSet: WeatherForecast)
    : RecyclerView.Adapter<ExtraWeatherAdapter.ExtraWeatherViewHolder>(){

    private val VIEW_TYPE_MAIN : Int = 0
    private val VIEW_TYPE_LIST : Int = 1

    inner class ExtraWeatherViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val todayDate = itemView.findViewById<TextView>(R.id.detail_today_text_view)
        val hour = itemView.findViewById<TextView>(R.id.detail_extra_hour_text_view)
        val temperature = itemView.findViewById<TextView>(R.id.detail_extra_temp_text_view)
        val weatherIcon = itemView.findViewById<ImageView>(R.id.detail_weather_icon)
        val weatherDescription = itemView.findViewById<TextView>(R.id.detail_weather_description_text_view)
        val highestTemp = itemView.findViewById<TextView>(R.id.detail_highest_temp_text_view)
        val lowestTemp = itemView.findViewById<TextView>(R.id.detail_lowest_temp_text_view)
    }

    fun setData(newDataSet: WeatherForecast){
        dataSet = newDataSet
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtraWeatherViewHolder {

        val layoutId = when(viewType){
            VIEW_TYPE_MAIN -> R.layout.extra_forecast_main_list_item
            else -> R.layout.extra_forecast_list_item
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
        return ExtraWeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExtraWeatherViewHolder, position: Int) {

        when(getItemViewType(position)){
            VIEW_TYPE_MAIN -> {
                holder.todayDate.text = dataSet.city

                val weatherIcon = Utils.getWeatherIcon(dataSet)
                holder.weatherIcon.setImageResource(weatherIcon)

                val weatherDescription = Utils.getWeatherDescription(dataSet)
                holder.weatherDescription.text = weatherDescription

                val highestTemp = Utils.findHighestTempForSingleCity(dataSet)
                val formattedHighestTemp = Utils.formatTemperature(holder.highestTemp.context,
                    highestTemp)
                holder.highestTemp.text = formattedHighestTemp

                val lowestTemp = Utils.findLowestTempForSingleCity(dataSet)
                val formattedLowestTemp = Utils.formatTemperature(holder.lowestTemp.context,
                    lowestTemp)
                holder.lowestTemp.text = formattedLowestTemp
            }
            else -> {
                holder.hour.text = Utils.getTimeDescription(dataSet.hourlyTemp[position - 1].hour)

                val hourlyTemp = dataSet.hourlyTemp[position -1].temp
                val formattedHourlyTemp =
                    Utils.formatTemperature(holder.temperature.context, hourlyTemp)
                holder.temperature.text = formattedHourlyTemp
            }
        }


    }

    override fun getItemCount(): Int {
        return dataSet.hourlyTemp.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> VIEW_TYPE_MAIN
            else -> VIEW_TYPE_LIST
        }
    }
}