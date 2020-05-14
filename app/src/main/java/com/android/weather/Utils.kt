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
    in 1..45 -> R.string.north_east
    in 46..90 -> R.string.east
    in 91..135 -> R.string.south_east
    in 136..180 -> R.string.south
    in 181..225 -> R.string.south_west
    in 226..270 -> R.string.west
    in 271..315 -> R.string.north_west
    else -> R.string.north
}
