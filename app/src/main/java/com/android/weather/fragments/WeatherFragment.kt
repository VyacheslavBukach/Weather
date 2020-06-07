package com.android.weather.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.weather.*
import com.android.weather.databinding.FragmentWeatherBinding
import com.android.weather.network.GeoApi
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.android.weather.R
import kotlinx.coroutines.CoroutineScope


private const val LOCATION_PERMISSION_REQUEST = 1
private const val LOCATION_PERMISSION = "android.permission.ACCESS_FINE_LOCATION"
const val IMAGE_URL = "https://openweathermap.org/img/wn/"

class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val uiScope = CoroutineScope(Dispatchers.Main)

    companion object {
        private var _latitude: Double = 0.0
        val latitude: Double
            get() = _latitude

        private var _longitude: Double = 0.0
        val longitude: Double
            get() = _longitude
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWeatherBinding.inflate(inflater, container, false)

        binding.getCoordinatesButton.setOnClickListener { getLastLocation() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()
    }

    fun loadWeatherAndUpdate() {
        // Launch Kotlin Coroutine on Android's main thread
        uiScope.launch {
            // Execute web request through coroutine call adapter & retrofit
            try {
                val webResponse = GeoApi.retrofitService.getWeatherByGps(
                    _latitude, _longitude,
                    "metric",
                    "ru",
                    "f20ee5d768c40c7094c1380400bf5a58"
                ).await()
                Log.i(TAG, "weatherFrag ${webResponse.message()}")
                if (webResponse.isSuccessful) {
                    // Get the returned & parsed JSON from the web response.
                    // Type specified explicitly here to make it clear that we already
                    // get parsed contents.
                    val partList = webResponse.body()

                    val weather = partList?.weather
                    val main = partList?.main
                    val wind = partList?.wind

                    binding.apply {
                        desciptionView.text = weather?.get(0)?.desc
                        temperatureView.text =
                            getString(R.string.temperature, main?.temperature?.oneSignAfterDot())
                        temperatureFeelsLikeView.text =
                            getString(
                                R.string.temperature_feels_like,
                                main?.temperature_feels_like?.oneSignAfterDot()
                            )
                        pressureView.text = getString(R.string.pressure, main?.pressure?.mmHg())
                        humidityView.text =
                            getString(R.string.humidity, main?.humidity?.noSignAfterDot())
                        speedView.text = getString(
                            R.string.speed,
                            wind?.speed?.oneSignAfterDot(),
                            wind?.direction?.findDirection()?.let { getString(it) } ?: ""
                        )
                        myCityView.text = partList?.city

                        Glide.with(requireContext())
                            .load("$IMAGE_URL${weather?.get(0)?.icon}@2x.png")
                            .into(imageView)
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

    private fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(requireContext(),
                LOCATION_PERMISSION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST ->
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Granted. Start getting the location information
                    getLastLocation()
                } else {

                }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location == null) {
                        requestNewLocationData()
                    } else {
//                        binding.latitudeView.text = location.latitude.toString()
//                        binding.longitudeView.text = location.longitude.toString()
//                        val locManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//                        val w = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//                        w.latitude
                        _latitude = location.latitude
                        _longitude = location.longitude
                        loadWeatherAndUpdate()
                    }
                }
            } else {
                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
//            binding.latitudeView.text = mLastLocation.latitude.toString()
//            binding.longitudeView.text = mLastLocation.longitude.toString()
        }
    }

}
