package com.example.android.weathertask.Repository

import androidx.lifecycle.LiveData
import com.example.android.weathertask.Data.Data
import com.example.android.weathertask.Data.WeatherForecast
import com.example.android.weathertask.Utils
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

object Repository {

    var job: CompletableJob? = null

    fun getData(): LiveData<Array<WeatherForecast>> {
        job = Job()
        return object : LiveData<Array<WeatherForecast>>() {
            override fun onActive() {
                super.onActive()
                job?.let { thisJob ->
                    CoroutineScope(IO + thisJob).launch {
                        val cities = loadCities()
                        withContext(Main) {
                            value = cities
                            thisJob.complete()
                        }
                    }
                }
            }
        }
    }

    fun cancelJobs() {
        job?.cancel()
    }

    fun loadCities(): Array<WeatherForecast> {
        val data = Data()
        return Utils.decodeJsonToModel(data.getJsonData())
    }
}