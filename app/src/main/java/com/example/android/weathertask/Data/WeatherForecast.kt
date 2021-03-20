package com.example.android.weathertask.Data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherForecast(val city: String, val weather: String, @SerialName("hourly_temp") val hourlyTemp: List<HourlyTemp>) {

}