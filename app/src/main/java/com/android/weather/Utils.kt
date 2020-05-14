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