package com.example.registropressoes.ui.activity

const val CHAVE_PRESSAO_ID = "pressao_id"

enum class EnumFiltrosPressao (val mode: String) {
    HOJE("HOJE"),
    SEMANA("SEMANA"),
    MES("MES"),
    TODOS("TODOS"),
    IMPORTADOS("IMPORTADOS")
}