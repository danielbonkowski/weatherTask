package com.example.android.weathertask.UI

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.weathertask.Data.HourlyTemp
import com.example.android.weathertask.Data.WeatherForecast
import com.example.android.weathertask.R
import com.example.android.weathertask.ViewModel.MainFragmentViewModel
import com.example.android.weathertask.ViewModel.SharedViewModel
import com.example.android.weathertask.databinding.FragmentMainBinding


class MainFragment : Fragment(), MainWeatherAdapter.MainWeatherAdapterOnClickHandler {

    private val mainViewModel: MainFragmentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var weatherList: Array<WeatherForecast>
    private val emptyDataSet = Array(0)
    {
        WeatherForecast("", "", List(0)
        { HourlyTemp(0.0, 0.0) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )

        setHasOptionsMenu(true)

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val viewAdapter = MainWeatherAdapter(emptyDataSet, this)
        val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)

        showProgressBar(binding)
        setupMainViewModel(binding, viewAdapter)
        setupSharedViewModel(viewAdapter)
        setupRecyclerView(binding, linearLayoutManager, viewAdapter, dividerItemDecoration)

        return binding.root
    }

    private fun setupMainViewModel(binding: FragmentMainBinding, viewAdapter: MainWeatherAdapter) {
        mainViewModel.cities.observe(viewLifecycleOwner, { cities ->
            weatherList = cities
            hideProgressBar(binding)

            if (cities == null || cities.isEmpty()) {
                showEmptyView(binding)
            } else {
                hideEmptyView(binding)
                viewAdapter.setData(weatherList)
            }
        })
    }

    private fun setupSharedViewModel(viewAdapter: MainWeatherAdapter) {
        sharedViewModel.isPreferredUnitMetric().observe(viewLifecycleOwner, {
            viewAdapter.notifyDataSetChanged()
        })
    }

    private fun setupRecyclerView(binding: FragmentMainBinding, linearLayoutManager: LinearLayoutManager,
        viewAdapter: MainWeatherAdapter, dividerItemDecoration: DividerItemDecoration) {
        binding.weatherMainRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = viewAdapter
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun showProgressBar(binding: FragmentMainBinding) {
        binding.progressBar.visibility = View.VISIBLE
        binding.weatherMainRecyclerView.visibility = View.GONE
    }

    private fun hideProgressBar(binding: FragmentMainBinding) {
        binding.progressBar.visibility = View.GONE
        binding.weatherMainRecyclerView.visibility = View.VISIBLE
    }

    private fun showEmptyView(binding: FragmentMainBinding) {
        binding.emptyView.visibility = View.VISIBLE
        binding.weatherMainRecyclerView.visibility = View.GONE
    }

    private fun hideEmptyView(binding: FragmentMainBinding) {
        binding.emptyView.visibility = View.GONE
        binding.weatherMainRecyclerView.visibility = View.VISIBLE
    }

    override fun onClick(weatherForecast: WeatherForecast, view: View) {
        sharedViewModel.selectCity(weatherForecast)
        view.findNavController()
            .navigate(MainFragmentDirections.actionMainFragmentToDetailFragment())
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