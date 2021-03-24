package com.example.android.weathertask.UI

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.weathertask.Data.HourlyTemp
import com.example.android.weathertask.Data.WeatherForecast
import com.example.android.weathertask.R
import com.example.android.weathertask.Utils
import com.example.android.weathertask.ViewModel.SharedViewModel
import com.example.android.weathertask.databinding.FragmentDetailBinding

/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val emptyExtraDataSet = WeatherForecast("", "", List<HourlyTemp>(0)
    { HourlyTemp(0.0, 0.0) })
    lateinit var weatherData: WeatherForecast

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container, false
        )

        setHasOptionsMenu(true)

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        val viewAdapter = ExtraWeatherAdapter(emptyExtraDataSet)
        val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)

        sharedViewModel.getSelectedCity().observe(viewLifecycleOwner, Observer { weatherForecast ->

            if (weatherForecast != null) {
                weatherData = weatherForecast
                viewAdapter.setData(weatherForecast)
            }
        })

        binding.extraWeatherRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = viewAdapter
            addItemDecoration(dividerItemDecoration)
        }

        return binding.root
    }

    private fun getShareIntent(): Intent {
        val city = weatherData.city
        val temperature = Utils.findHighestTempForSingleCity(weatherData)
        val formattedTemp = context?.let { Utils.formatTemperature(it, temperature) }
        val message = "Today in $city the highest temperature will reach $formattedTemp"

        return ShareCompat.IntentBuilder.from(requireActivity())
            .setText(message)
            .setType("text/plain")
            .intent
    }

    private fun shareSuccess(){
        startActivity(getShareIntent())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.details, menu)

        if(null == getShareIntent().resolveActivity(activity!!.packageManager)){
            menu?.findItem(R.id.share).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }
}