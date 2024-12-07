package com.example.registropressoes.Extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Long.parseToLocaleDateTime(): LocalDateTime {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.UTC)
}