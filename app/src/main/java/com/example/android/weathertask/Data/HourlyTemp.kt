package com.example.android.weathertask.Data

import kotlinx.serialization.Serializable

@Serializable
data class HourlyTemp(val temp: Double, val hour: Double) {
}