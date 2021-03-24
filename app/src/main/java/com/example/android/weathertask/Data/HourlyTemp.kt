package com.example.android.weathertask.Data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyTemp(@SerialName("temp") val temp: Double, @SerialName("hour") val hour: Double) {
}