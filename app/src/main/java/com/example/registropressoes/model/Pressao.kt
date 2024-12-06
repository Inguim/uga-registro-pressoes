package com.example.registropressoes.model

import com.example.registropressoes.Extensions.toLocaleDateTime
import com.example.registropressoes.Extensions.toStringDateTimeBR
import kotlinx.datetime.Clock

data class Pressao(
    val maxima: Double,
    val minima: Double,
    val data: Long = Clock.System.now().toEpochMilliseconds()
) {
    val dataToBr: String get() = data.toLocaleDateTime().toStringDateTimeBR()
}