package com.example.registropressoes.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.registropressoes.ui.activity.EnumFiltrosPressao
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

data class PressaoFiltro(
    var mode: EnumFiltrosPressao = EnumFiltrosPressao.HOJE,
) {
    private var _data: LocalDateTime =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    private var _inicio: Long? = null
    private var _fim: Long? = null

    init {
        definir()
    }

    fun definir(to: EnumFiltrosPressao = EnumFiltrosPressao.HOJE) {
        mode = to
        when (mode) {
            EnumFiltrosPressao.HOJE -> gerarHoje()
            EnumFiltrosPressao.SEMANA -> gerarSemana()
            EnumFiltrosPressao.MES -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                gerarMes()
            }
            EnumFiltrosPressao.IMPORTADOS -> gerarImportados()
            EnumFiltrosPressao.TODOS -> gerarTodos()
        }
    }

    fun getPeriodo(): List<Long?> {
        return listOf(_inicio, _fim)
    }

    private fun gerarHoje() {
        val start = _data.date.atTime(0, 0, 0)
        val end = _data.date.atTime(23, 59, 59)
        _inicio = start.toInstant(TimeZone.UTC).toEpochMilliseconds()
        _fim = end.toInstant(TimeZone.UTC).toEpochMilliseconds()
    }

    private fun gerarSemana() {
        val day = _data.dayOfWeek.isoDayNumber - 1
        val start = LocalDateTime(_data.year, _data.month, _data.date.dayOfMonth - day, 0, 0)
        val end = LocalDateTime(start.year, start.month, start.date.dayOfMonth + 6, 23, 59)
        _inicio = start.toInstant(TimeZone.UTC).toEpochMilliseconds()
        _fim = end.toInstant(TimeZone.UTC).toEpochMilliseconds()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun gerarMes() {
        val start = LocalDateTime(_data.year, _data.month, 1, 0, 0)
        val end = LocalDateTime(_data.year, _data.month, _data.month.maxLength(), 0, 0)
        _inicio = start.toInstant(TimeZone.UTC).toEpochMilliseconds()
        _fim = end.toInstant(TimeZone.UTC).toEpochMilliseconds()
    }

    private fun gerarTodos() {
        _inicio = null
        _fim = null
    }

    private fun gerarImportados() {
        _inicio = null
        _fim = null
    }
}
