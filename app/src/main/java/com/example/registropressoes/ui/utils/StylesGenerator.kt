package com.example.registropressoes.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.ContextCompat
import com.example.registropressoes.R
import com.example.registropressoes.extensions.parseToUmaCasaDecimal
import com.example.registropressoes.model.Pressao

@SuppressLint("ResourceAsColor")
fun generateMedicaoIndicador(context: Context, pressao: Pressao): Pair<Int, String> {
    var texto = context.getString(
        R.string.pressa_item_valor,
        pressao.maxima.parseToUmaCasaDecimal(),
        pressao.minima.parseToUmaCasaDecimal()
    )
    var cor = R.color.pressao_saudavel
    if (pressao.maxima >= 14 && pressao.minima >= 9) {
        texto = context.getString(
            R.string.pressa_item_valor_risco,
            pressao.maxima.parseToUmaCasaDecimal(),
            pressao.minima.parseToUmaCasaDecimal()
        )
        cor = R.color.pressao_risco
    } else if (pressao.maxima <= 10 && pressao.minima <= 6) {
        texto = context.getString(
            R.string.pressa_item_valor,
            pressao.maxima.parseToUmaCasaDecimal(),
            pressao.minima.parseToUmaCasaDecimal()
        )
        cor = R.color.pressao_alerta
    }
    cor = ContextCompat.getColor(context, cor)
    return Pair(cor, texto)
}