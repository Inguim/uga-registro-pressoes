package com.example.registropressoes.extensions

import java.text.DecimalFormat

fun Double.parseToUmaCasaDecimal(): String {
    val formatterDecimal = DecimalFormat("#.#")
    return formatterDecimal.format(this)
}