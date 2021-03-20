package com.example.android.weathertask.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.weathertask.Data.JsonData
import com.example.android.weathertask.Data.WeatherForecast
import com.example.android.weathertask.Utils
import com.example.android.weathertask.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), MainWeatherAdapter.MainWeatherAdapterOnClickHandler {

    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val jsonData = JsonData()
        val weatherList: Array<WeatherForecast> = Utils.decodeJsonToModel(jsonData.getJsonData())


        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        val viewAdapter = MainWeatherAdapter(weatherList, this)
        val dividerItemDecoration = DividerItemDecoration(this, linearLayoutManager.orientation)


        binding.weatherRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = viewAdapter
            addItemDecoration(dividerItemDecoration)
        }
    }

    override fun onClick(weatherForecast: WeatherForecast) {
        Toast.makeText(this, weatherForecast.city, Toast.LENGTH_SHORT).show()
    }
}