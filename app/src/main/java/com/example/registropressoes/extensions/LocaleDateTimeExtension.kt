package com.example.registropressoes.extensions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toInstant

@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalDateTime.toStringDateTimeBR(): String {
    return this.format(LocalDateTime.Format {
        byUnicodePattern("dd/MM/yyyy - HH:mm")
    })
}

fun LocalDateTime.toLong(): Long {
    return this.toInstant(TimeZone.UTC).toEpochMilliseconds()
}