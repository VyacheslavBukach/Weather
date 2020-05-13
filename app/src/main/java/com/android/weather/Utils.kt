package com.android.weather

import java.math.BigDecimal

fun Double.round() = this
    .minus(273.15)
    .toBigDecimal()
    .setScale(2, BigDecimal.ROUND_HALF_UP)
    .toString()