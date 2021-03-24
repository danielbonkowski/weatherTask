package com.example.android.weathertask.UI

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.weathertask.Data.HourlyTemp
import com.example.android.weathertask.Data.WeatherForecast
import com.example.android.weathertask.R
import com.example.android.weathertask.Utils
import com.example.android.weathertask.ViewModel.MainFragmentViewModel
import com.example.android.weathertask.ViewModel.SharedViewModel
import com.example.android.weathertask.databinding.FragmentMainBinding


/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(), MainWeatherAdapter.MainWeatherAdapterOnClickHandler{

    private val mainViewModel: MainFragmentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var weatherList : Array<WeatherForecast>
    private val emptyDataSet = Array<WeatherForecast>(0)
        { WeatherForecast("", "", List<HourlyTemp>(0)
        { HourlyTemp(0.0, 0.0) }) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )

        setHasOptionsMenu(true)


        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        val viewAdapter = MainWeatherAdapter( emptyDataSet, this)
        val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)


        mainViewModel.cities.observe(viewLifecycleOwner, Observer { cities ->
            weatherList = cities
            viewAdapter.setData(weatherList)
            if(weatherList.isEmpty()){
                binding.emptyView.visibility = View.VISIBLE
                binding.weatherMainRecyclerView.visibility = View.GONE
            }else{
                binding.emptyView.visibility = View.GONE
                binding.weatherMainRecyclerView.visibility = View.VISIBLE
            }
        })

        sharedViewModel.isPreferredUnitMetric().observe(viewLifecycleOwner, Observer {
            viewAdapter.notifyDataSetChanged()
        })

        binding.weatherMainRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = viewAdapter
            addItemDecoration(dividerItemDecoration)
        }

        return binding.root
    }

    override fun onClick(weatherForecast: WeatherForecast, view: View) {
        sharedViewModel.selectCity(weatherForecast)
        view.findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailFragment())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, view!!.findNavController()) ||
                super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.cancelJobs()
    }

}