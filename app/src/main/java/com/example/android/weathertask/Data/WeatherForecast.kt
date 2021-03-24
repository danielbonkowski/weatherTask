package com.example.android.weathertask.Data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherForecast(
    @SerialName("city") val city: String,
    @SerialName("weather") val weather: String,
    @SerialName("hourly_temp") val hourlyTemp: List<HourlyTemp>
) {

}