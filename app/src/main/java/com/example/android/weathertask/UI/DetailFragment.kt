package com.example.android.weathertask.UI

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.weathertask.Data.HourlyTemp
import com.example.android.weathertask.Data.WeatherForecast
import com.example.android.weathertask.R
import com.example.android.weathertask.Utils.Utils
import com.example.android.weathertask.ViewModel.SharedViewModel
import com.example.android.weathertask.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val emptyExtraDataSet = WeatherForecast("", "", List(0)
    { HourlyTemp(0.0, 0.0) })
    private lateinit var weatherData: WeatherForecast

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container, false
        )

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val viewAdapter = ExtraWeatherAdapter(requireContext(), emptyExtraDataSet)
        val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)

        setHasOptionsMenu(true)

        setupRecyclerView(binding, linearLayoutManager, viewAdapter, dividerItemDecoration)
        setupSharedViewModel(viewAdapter)

        return binding.root
    }

    private fun setupRecyclerView(
        binding: FragmentDetailBinding, linearLayoutManager: LinearLayoutManager,
        viewAdapter: ExtraWeatherAdapter, dividerItemDecoration: DividerItemDecoration
    ) {

        binding.extraWeatherRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = viewAdapter
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun setupSharedViewModel(viewAdapter: ExtraWeatherAdapter) {
        sharedViewModel.getSelectedCity().observe(viewLifecycleOwner, { weatherForecast ->
            if (weatherForecast != null) {
                weatherData = weatherForecast
                viewAdapter.setData(weatherForecast)
            }
        })
    }

    private fun getShareIntent(city: String, temperature: Double): Intent {
        val formattedTemp = context?.let { Utils.formatTemperature(it, temperature) }
        val message = "Today in $city the highest temperature will reach $formattedTemp"

        return ShareCompat.IntentBuilder.from(requireActivity())
            .setText(message)
            .setType("text/plain")
            .intent
    }

    private fun shareSuccess() {
        startActivity(
            getShareIntent(
                weatherData.city,
                Utils.findHighestTempForSingleCity(weatherData)
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.details, menu)

        if (null == getShareIntent(
                weatherData.city,
                Utils.findHighestTempForSingleCity(weatherData)
            )
                .resolveActivity(activity!!.packageManager)
        ) {
            menu.findItem(R.id.share).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }
}