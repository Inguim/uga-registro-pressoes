package com.example.registropressoes.Extensions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalDateTime.toStringDateTimeBR(): String {
    return this.format(LocalDateTime.Format {
        byUnicodePattern("dd/MM/yyyy - HH:mm")
    })
}