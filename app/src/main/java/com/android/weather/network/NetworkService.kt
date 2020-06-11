package com.android.weather.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://api.openweathermap.org/"

interface GeoApiInterface {
//    https://api.openweathermap.org/data/2.5/weather?q=Grodno&APPID=f20ee5d768c40c7094c1380400bf5a58
    @GET("/data/2.5/weather")
    fun getWeatherByCity(
        @Query("q") city: String,
        @Query("units") format: String,
        @Query("lang") language: String,
        @Query("APPID") appid: String
    ): Deferred<Response<WeatherResponse>>

    @GET("/data/2.5/forecast")
    fun getForecastByCity(
        @Query("q") city: String,
        @Query("units") format: String,
        @Query("lang") language: String,
        @Query("APPID") appid: String
    ): Deferred<Response<ForecastResponse>>

    @GET("data/2.5/weather")
    fun getWeatherByGps(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") format: String,
        @Query("lang") language: String,
        @Query("APPID") appid: String
    ): Deferred<Response<WeatherResponse>>

    @GET("data/2.5/forecast")
    fun getForecastByGps(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") format: String,
        @Query("lang") language: String,
        @Query("APPID") appid: String
    ): Deferred<Response<ForecastResponse>>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

object GeoApi {
    val retrofitService: GeoApiInterface by lazy { retrofit.create(GeoApiInterface::class.java) }
}