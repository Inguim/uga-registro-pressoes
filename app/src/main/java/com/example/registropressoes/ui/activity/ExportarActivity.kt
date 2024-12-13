package com.example.registropressoes.ui.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.orgs.database.AppDataBase
import com.example.registropressoes.R
import com.example.registropressoes.databinding.ActivityExportarBinding
import com.example.registropressoes.extensions.parseToExportString
import com.example.registropressoes.extensions.parseToLocaleDateTime
import com.example.registropressoes.extensions.parseToUmaCasaDecimal
import com.example.registropressoes.extensions.toast
import kotlinx.coroutines.launch

class ExportarActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityExportarBinding.inflate(layoutInflater)
    }
    private val dao by lazy {
        AppDataBase.instancia(this).pressaoDAO()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configurarBotaoCopiar()
        lifecycleScope.launch {
            buscarPressoes()
        }
    }

    private suspend fun buscarPressoes() {
        showLoading()
        dao.listar().collect {
            val texto = StringBuilder("Acompanhamento pressão\n\n")
            it.map {
                val dataFormatada = it.data.parseToLocaleDateTime().parseToExportString()
                texto.appendLine("$dataFormatada hrs - ${it.maxima.parseToUmaCasaDecimal()}x${it.minima.parseToUmaCasaDecimal()}")
            }
            val campo = binding.activityExportarTexto
            campo.setText(texto)
            hideLoading()
        }
    }

    private fun configurarBotaoCopiar() {
        binding.activityExportarBotaoExportar.setOnClickListener {
            copiar()
        }
    }

    @SuppressLint("ServiceCast")
    private fun copiar() {
        val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
        val texto = binding.activityExportarTexto.text
        clipboard?.setPrimaryClip(ClipData.newPlainText("", texto))
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            toast(getString(R.string.exportar_message_copiado))
        }
    }

    private fun showLoading() {
        binding.activityExportarLoading.visibility = View.VISIBLE
        binding.activityExportarBotaoExportar.isEnabled = false
    }

    private fun hideLoading() {
        binding.activityExportarLoading.visibility = View.GONE
        binding.activityExportarBotaoExportar.isEnabled = true
        binding.activityExportarTextinputlayoutTexto.hint =
            getString(R.string.exportar_texto_encontrado)
    }
}