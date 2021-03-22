package com.example.android.weathertask.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.weathertask.Data.WeatherForecast

class SharedViewModel : ViewModel() {

    private val selected = MutableLiveData<WeatherForecast>();

    fun selectCity(city: WeatherForecast){
        selected.value = city
    }

    fun getSelected() = selected
}