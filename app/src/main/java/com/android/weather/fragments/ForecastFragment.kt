package com.android.weather.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.weather.Day
import com.android.weather.adapters.ForecastAdapter
import com.android.weather.databinding.FragmentForecastBinding
import com.android.weather.network.ForecastResponse
import com.android.weather.network.GeoApi
import com.android.weather.network.WeatherResponse
import com.android.weather.oneSignAfterDot
import com.android.weather.parseDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

const val TAG = "FORECAST"

class ForecastFragment : Fragment() {

    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!
    private val uiScope = CoroutineScope(Dispatchers.Main)

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

    fun loadWeatherAndUpdate(city: String = "") {
        // Launch Kotlin Coroutine on Android's main thread
        uiScope.launch {
            // Execute web request through coroutine call adapter & retrofit
            try {
                val webResponse: Response<ForecastResponse>
                if(city == "") {
                    webResponse = GeoApi.retrofitService.getForecastByGps(
                        WeatherFragment.latitude, WeatherFragment.longitude,
                        "metric",
                        "ru",
                        "f20ee5d768c40c7094c1380400bf5a58"
                    ).await()
                }
                else {
                    webResponse = GeoApi.retrofitService.getForecastByCity(
                        city,
                        "metric",
                        "ru",
                        "f20ee5d768c40c7094c1380400bf5a58"
                    ).await()
                }
                Log.i(TAG, "forecastFrag ${webResponse.message()}")
                if (webResponse.isSuccessful) {
                    // Get the returned & parsed JSON from the web response.
                    // Type specified explicitly here to make it clear that we already
                    // get parsed contents.
                    val partList = webResponse.body()

                    val list = partList?.list

                    val listOfDays = mutableListOf<Day>()
                    var date: String?

                    for(i in 0 until 40) {
                        date = parseDate(list?.get(i)?.daytime) ?: ""
                        listOfDays.add(i, Day(
                            date,
                            list?.get(i)?.main?.temperature?.oneSignAfterDot(),
                            "$IMAGE_URL${list?.get(i)?.weather?.get(0)?.icon}@2x.png"
                        ))
                    }

                    binding.recyclerView.apply {
                        layoutManager = LinearLayoutManager(activity)
                        adapter = ForecastAdapter(listOfDays)
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
