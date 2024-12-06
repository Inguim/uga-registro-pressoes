package com.example.registropressoes.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime


data class Pressao(
    val maxima: Double,
    val minima: Double,
    val data: Long = Clock.System.now().toEpochMilliseconds()
) {
    @OptIn(FormatStringsInDatetimeFormats::class)
    val dataToBr: String get() = data.let {
        Instant.fromEpochMilliseconds(it)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .format(LocalDateTime.Format {
                byUnicodePattern("dd/MM/yyyy - HH:mm")
            })

    }
}