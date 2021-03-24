package com.example.android.weathertask.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.weathertask.Data.WeatherForecast

class SharedViewModel : ViewModel() {

    private val selectedCity = MutableLiveData<WeatherForecast>();
    private val isPreferredUnitMetric = MutableLiveData<Boolean>()

    fun selectCity(city: WeatherForecast) {
        selectedCity.value = city
    }

    fun selectUnit(isMetric: Boolean) {
        isPreferredUnitMetric.value = isMetric
    }

    fun getSelectedCity() = selectedCity
    fun isPreferredUnitMetric() = isPreferredUnitMetric

}