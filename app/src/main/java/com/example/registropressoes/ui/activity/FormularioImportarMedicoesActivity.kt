package com.example.registropressoes.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.database.AppDataBase
import com.example.registropressoes.R
import com.example.registropressoes.databinding.ActivityImportarMedicoesBinding
import com.example.registropressoes.extensions.toast
import com.example.registropressoes.model.PressaoImportada
import kotlinx.coroutines.launch

class FormularioImportarMedicoesActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityImportarMedicoesBinding.inflate(layoutInflater)
    }
    private val dao by lazy {
        AppDataBase.instancia(this).pressaoDAO()
    }
    val medicoes = mutableListOf<PressaoImportada>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configurarBotaoImportar()
    }

    private fun validarCampo(): Boolean {
        var error = false
        val campo = binding.activityFormularioImportarMedicoesTexto
        val valor = campo.text.toString()
        if (valor.isBlank()) {
            error = true
            campo.error = getString(R.string.formulario_importar_error_medicoes)
            campo.requestFocus()
        }
        return error
    }

    private fun extrairMedicoes(texto: String): List<PressaoImportada> {
        val regex = Regex("""(\d{2}/\d{2}) - (\d{2}:\d{2} hrs) - (.+)""")
        val dadosPressao = mutableListOf<PressaoImportada>()
        texto.lines().forEach { linha ->
            val matchResult = regex.find(linha)
            if (matchResult != null) {
                val (data, hora, pressao) = matchResult.destructured
                dadosPressao.add(PressaoImportada(data, hora, pressao))
            }
        }
        return dadosPressao
    }

    private fun importar() {
        val campo = binding.activityFormularioImportarMedicoesTexto
        val dadosExtraidos = extrairMedicoes(campo.text.toString())
        medicoes.clear()
        medicoes.addAll(dadosExtraidos)
        val pressoes = medicoes.map { it.parseToPressao() }
        try {
            lifecycleScope.launch {
                dao.multiplaAdicao(pressoes)
                finish()
            }
            finish()
        } catch (e: Exception) {
            toast(getString(R.string.formulario_importar_error_importacao))
        }
    }

    private fun configurarBotaoImportar() {
        binding.activityFormularioImportarMedicoesBotaoImportar.setOnClickListener {
            if (!validarCampo()) {
                importar()
            }
        }
    }
}