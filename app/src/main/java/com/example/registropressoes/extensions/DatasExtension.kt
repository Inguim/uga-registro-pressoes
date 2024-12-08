package com.example.registropressoes.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalDateTime.toStringDateTimeBR(): String {
    return this.format(LocalDateTime.Format {
        byUnicodePattern("dd/MM/yyyy - HH:mm")
    })
}

fun LocalDateTime.toLong(): Long {
    return this.toInstant(TimeZone.UTC).toEpochMilliseconds()
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun String.stringtoLocalDateTime(): LocalDateTime {
    val formatter = LocalDateTime.Format { byUnicodePattern("dd/MM/yyyy - HH:mm") }
    return formatter.parse(this)
}

fun Long.parseToLocaleDateTime(): LocalDateTime {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.UTC)
}