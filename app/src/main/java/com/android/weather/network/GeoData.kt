package com.android.weather.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GeoData(
    @Json(name = "name") val city: String?) : Parcelable