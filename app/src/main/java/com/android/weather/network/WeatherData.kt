package com.android.weather.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class WeatherResponse(
    @Json(name = "weather") val weather: List<Weather>,
    @Json(name = "main") val main: Main,
    @Json(name = "wind") val wind: Wind,
    @Json(name = "name") val city: String
): Parcelable

@Parcelize
data class ForecastResponse(
    @Json(name = "list") val list: List<Day>,
    @Json(name = "city") val city: City
): Parcelable

@Parcelize
data class Weather(
    @Json(name = "description") val desc: String,
    @Json(name = "icon") val icon: String
) : Parcelable

@Parcelize
data class Main(
    @Json(name = "temp") val temperature: Double,
    @Json(name = "feels_like") val temperature_feels_like: Double,
    @Json(name = "pressure") val pressure: Double,
    @Json(name = "humidity") val humidity: Double
) : Parcelable

@Parcelize
data class Wind(
    @Json(name = "speed") val speed: Double,
    @Json(name = "deg") val direction: Double?
) : Parcelable

@Parcelize
data class City(
    @Json(name = "name") val city: String
) : Parcelable

@Parcelize
data class Day(
    @Json(name = "main") val main: Main,
    @Json(name = "weather") val weather: List<Weather>,
    @Json(name = "wind") val wind: Wind,
    @Json(name = "dt_txt") val daytime: String
) : Parcelable