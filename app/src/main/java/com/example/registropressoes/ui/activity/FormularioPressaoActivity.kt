package com.example.registropressoes.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.database.AppDataBase
import com.example.registropressoes.Extensions.toLocalDateTime
import com.example.registropressoes.Extensions.toLocaleDateTime
import com.example.registropressoes.Extensions.toLong
import com.example.registropressoes.Extensions.toStringDateTimeBR
import com.example.registropressoes.Extensions.toast
import com.example.registropressoes.R
import com.example.registropressoes.databinding.ActivityFormularioPressaoBinding
import com.example.registropressoes.model.Pressao
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class FormularioPressaoActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFormularioPressaoBinding.inflate(layoutInflater)
    }
    private val dao by lazy {
        AppDataBase.instancia(this).pressaoDAO()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configurarBotaoSalvar()
        configurarDatePicker()
    }

    private fun configurarDatePicker() {
        val campoData = binding.activityFormularioPressaoData
        campoData.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "MATERIAl_DATE_PICKER")
            datePicker.addOnPositiveButtonClickListener { milli ->
                campoData.setText(milli.toLocaleDateTime().toStringDateTimeBR())
            }
        }
    }

    private fun configurarBotaoSalvar() {
        binding.activityFormularioPressaoBotaoSalvar.setOnClickListener {
            if (!validarCampos()) {
                cadastra(criaPressao())
            }
        }
    }

    private fun validarCampos(): Boolean {
        var error = false
        var campo: TextInputEditText? = null
        val dataValido = validarCampoData()
        val maximaValido = validarCampoMaximo()
        val minimoValido = validarCampoMinimo()
        if (!dataValido || !maximaValido || !minimoValido) {
            error = true
        }
        if (!minimoValido) {
            campo = binding.activityFormularioPressaoData
            campo.error = getString(R.string.formulario_pressao_error_data)
        }
        if (!maximaValido) {
            campo = binding.activityPressaoProdutoMaxima
            campo.error = getString(R.string.formulario_pressao_error_maxima)
        }
        if (!dataValido) {
            campo = binding.activityPressaoProdutoMinima
            campo.error = getString(R.string.formulario_pressao_error_minima)
        }
        if (error) campo?.requestFocus()
        return error
    }

    private fun validarCampoData(): Boolean {
        val campo = binding.activityFormularioPressaoData
        try {
            val valor = campo.text.toString()
            valor.toLocalDateTime().toLong()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    private fun validarCampoMaximo(): Boolean {
        val campo = binding.activityPressaoProdutoMaxima
        val valor = campo.text.toString().toDoubleOrNull()
        return valor != null && valor > 0
    }

    private fun validarCampoMinimo(): Boolean {
        val campo = binding.activityPressaoProdutoMinima
        val valor = campo.text.toString().toDoubleOrNull()
        return valor != null && valor > 0
    }

    private fun cadastra(pressao: Pressao) {
        if (!validarCampos()) {
            lifecycleScope.launch {
                try {
                    dao.adicionar(pressao)
                    finish()
                } catch (e: Exception) {
                    toast(getString(R.string.formulario_pressao_error_falha_insercao))
                }
            }
        }
    }

    private fun criaPressao(): Pressao {
        val data = binding.activityFormularioPressaoData.text.toString()
        val maxima = binding.activityPressaoProdutoMaxima.text.toString().toDouble()
        val minina = binding.activityPressaoProdutoMinima.text.toString().toDouble()
        return Pressao(
            data.toLocalDateTime().toLong(),
            maxima,
            minina
        )
    }
}