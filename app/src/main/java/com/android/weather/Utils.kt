package com.android.weather

import java.math.BigDecimal

const val mmHgInhPa = 0.75006375541921

fun Double.celcius() = this
    .minus(273.15)
    .toBigDecimal()
    .setScale(1, BigDecimal.ROUND_HALF_UP)
    .toString()

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
