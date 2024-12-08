package com.example.registropressoes.extensions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

@OptIn(FormatStringsInDatetimeFormats::class)
fun String.stringtoLocalDateTime(): LocalDateTime {
    val formatter = LocalDateTime.Format { byUnicodePattern("dd/MM/yyyy - HH:mm") }
    return formatter.parse(this)
}
