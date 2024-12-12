package com.example.registropressoes.model

import com.example.registropressoes.extensions.parseToLocaleDateTime
import com.example.registropressoes.extensions.toLong
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlin.math.pow

data class PressaoImportada(val data: String, val hora: String, val pressao: String) {
    fun parseToPressao(): Pressao {
        val (ano, mes, dia) = parseData()
        val (horas, minutos) = parseHora()
        val (maxima, minima) = parseMedicao()
        val dataGerada = LocalDateTime(
            year = ano,
            monthNumber = mes,
            dayOfMonth = dia,
            hour = horas,
            minute = minutos
        )
        return Pressao(
            maxima = maxima,
            minima = minima,
            data = dataGerada.toLong(),
            importado = true
        )
    }

    private fun parseHora(): Pair<Int, Int> {
        val (horas, minutos) = this.hora.split(":")
        return Pair(horas.toInt(), minutos.split(" ")[0].toInt())
    }

    private fun parseData(): Triple<Int, Int, Int> {
        val (dia, mes) = this.data.split("/")
        val agora = Clock.System.now().toEpochMilliseconds().parseToLocaleDateTime()
        return Triple(
            agora.year,
            mes.toInt(),
            dia.toInt()
        )
    }

    private fun parseMedicao(): Pair<Double, Double> {
        val partes = this.pressao.split("x")
        val maxima = parseMedicaoDouble(partes[0])
        val minima = parseMedicaoDouble(partes[1])
        return Pair(maxima, minima)
    }

    private fun parseMedicaoDouble(valorStr: String): Double {
        return if (valorStr.length >= 2) {
            val parteInteira = valorStr.substring(0, 2).toIntOrNull() ?: 0
            val parteDecimal = valorStr.substring(2).toIntOrNull() ?: 0
            (parteInteira + parteDecimal.toDouble() / 10.0.pow(parteDecimal.toString().length))
        } else {
            valorStr.toDoubleOrNull() ?: 0.0
        }
    }
}