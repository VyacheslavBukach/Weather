package com.android.weather.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.weather.*
import com.android.weather.adapters.ForecastAdapter
import com.android.weather.databinding.FragmentForecastBinding
import com.android.weather.network.GeoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val TAG = "FORECAST"

class ForecastFragment : Fragment() {

    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadWeatherAndUpdate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadWeatherAndUpdate() {
        // Launch Kotlin Coroutine on Android's main thread
        GlobalScope.launch(Dispatchers.Main) {
            // Execute web request through coroutine call adapter & retrofit
            try {
                val webResponse = GeoApi.retrofitService.getForecastByGps(
                    WeatherFragment.latitude, WeatherFragment.longitude,
                    "metric",
                    "ru",
                    "f20ee5d768c40c7094c1380400bf5a58"
                ).await()

                if (webResponse.isSuccessful) {
                    // Get the returned & parsed JSON from the web response.
                    // Type specified explicitly here to make it clear that we already
                    // get parsed contents.
                    val partList = webResponse.body()

                    val list = partList?.list

                    val data = mutableListOf<Day>()
                    for(i in 0 until 40) {
                        data.add(i, Day(
                            list?.get(i)?.daytime,
                            list?.get(i)?.main?.temperature.toString(),
                            "$IMAGE_URL${list?.get(i)?.weather?.get(0)?.icon}@2x.png"))
                    }

                    binding.recyclerView.apply {
                        layoutManager = LinearLayoutManager(activity)
                        adapter = ForecastAdapter(data)
                    }

                } else {
                    Toast.makeText(context, "Error ${webResponse.code()}", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (t: Throwable) {
                Toast.makeText(context, "No connection", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
