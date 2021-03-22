package com.example.android.weathertask.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.weathertask.Data.WeatherForecast
import com.example.android.weathertask.Repository.Repository

class MainFragmentViewModel : ViewModel(){

    val cities: LiveData<Array<WeatherForecast>> = Repository.getData()

    fun cancelJobs(){
        Repository.cancelJobs()
    }
}