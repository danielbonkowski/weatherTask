package com.example.android.weathertask.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.weathertask.Data.JsonData
import com.example.android.weathertask.Data.WeatherForecast
import com.example.android.weathertask.R
import com.example.android.weathertask.Utils
import com.example.android.weathertask.databinding.FragmentMainBinding


/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(), MainWeatherAdapter.MainWeatherAdapterOnClickHandler {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )



        val jsonData = JsonData()
        val weatherList: Array<WeatherForecast> = Utils.decodeJsonToModel(jsonData.getJsonData())

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        val viewAdapter = MainWeatherAdapter(weatherList, this)
        val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)


        binding.weatherRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = viewAdapter
            addItemDecoration(dividerItemDecoration)
        }

        return binding.root
    }

    override fun onClick(weatherForecast: WeatherForecast, view: View) {
        view.findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailFragment())
    }
}