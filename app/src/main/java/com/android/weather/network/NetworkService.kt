package com.android.weather.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.openweathermap.org"

interface ApiService {
    @GET("/data/2.5/weather?q=Grodno&APPID=f20ee5d768c40c7094c1380400bf5a58")
    fun getGeo():
    // The Coroutine Call Adapter allows us to return a Deferred, a Job with a result
            Deferred<Response<GeoData>>
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
    val retrofitService : ApiService by lazy { retrofit.create(ApiService::class.java) }
}