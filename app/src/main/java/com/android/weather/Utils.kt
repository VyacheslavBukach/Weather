package com.android.weather

import java.math.BigDecimal
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


const val mmHgInhPa = 0.75006375541921

fun Double.mmHg() = this
    .times(mmHgInhPa)
    .toBigDecimal()
    .setScale(0, BigDecimal.ROUND_HALF_UP)
    .toString()

fun Double.oneSignAfterDot() = this
    .toBigDecimal()
    .setScale(1, BigDecimal.ROUND_HALF_UP)
    .toString()

fun Double.noSignAfterDot() = this
    .toBigDecimal()
    .setScale(0, BigDecimal.ROUND_HALF_UP)
    .toString()

fun Double.findDirection() = when(this) {
    in 1.0..45.0 -> R.string.north_east
    in 46.0..90.0 -> R.string.east
    in 91.0..135.0 -> R.string.south_east
    in 136.0..180.0 -> R.string.south
    in 181.0..225.0 -> R.string.south_west
    in 226.0..270.0 -> R.string.west
    in 271.0..315.0 -> R.string.north_west
    else -> R.string.north
}

fun parseDate(time: String?): String? {

    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val outputFormat = SimpleDateFormat("d MMMM HH:mm")
    try {
        val date = inputFormat.parse(time)
        return outputFormat.format(date)
    }
    catch (e : Exception) {
        return null
    }
}