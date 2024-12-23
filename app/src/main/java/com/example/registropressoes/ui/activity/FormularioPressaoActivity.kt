package com.example.registropressoes.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.database.AppDataBase
import com.example.registropressoes.R
import com.example.registropressoes.databinding.ActivityFormularioPressaoBinding
import com.example.registropressoes.extensions.parseToLocaleDateTime
import com.example.registropressoes.extensions.stringtoLocalDateTime
import com.example.registropressoes.extensions.toLong
import com.example.registropressoes.extensions.toStringDateTimeBR
import com.example.registropressoes.extensions.toast
import com.example.registropressoes.model.Pressao
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toLocalDateTime

class FormularioPressaoActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFormularioPressaoBinding.inflate(layoutInflater)
    }
    private val dao by lazy {
        AppDataBase.instancia(this).pressaoDAO()
    }
    private var id = 0L
    private var importado = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configurarBotaoSalvar()
        configurarDatePicker()
        isEditing()
    }

    override fun onResume() {
        super.onResume()
        buscarPressao()
    }

    private fun isEditing() {
        id = intent.getLongExtra(CHAVE_PRESSAO_ID, 0L)
        if (id == 0L) {
            preencherFormularioInclusao()
        } else {
            lifecycleScope.launch {
                verificarImportado()
            }
        }
    }

    private suspend fun verificarImportado() {
        val pressao = dao.listarPorId(id).firstOrNull()
        importado = pressao?.importado ?: false
    }

    private fun preencherFormularioInclusao() {
        with(binding) {
            activityFormularioPressaoData.setText(
                Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .toStringDateTimeBR()
            )
            lifecycleScope.launch {
                val ultimaMedicao = dao.listarUltimaMedicao().firstOrNull()
                if (ultimaMedicao != null) {
                    activityPressaoProdutoMaxima.setText(ultimaMedicao.maxima.toString())
                    activityPressaoProdutoMinima.setText(ultimaMedicao.minima.toString())
                }
            }
        }
    }

    private fun buscarPressao() {
        lifecycleScope.launch {
            dao.listarPorId(id).collect {
                it?.let {
                    preencherFormularioEdicao(it)
                }
            }
        }
    }

    private fun preencherFormularioEdicao(pressao: Pressao) {
        title = getString(R.string.edicao_medicao)
        with(binding) {
            activityFormularioPressaoData.setText(pressao.dataToBr)
            activityPressaoProdutoMaxima.setText(pressao.maxima.toString())
            activityPressaoProdutoMinima.setText(pressao.minima.toString())
        }
    }

    private fun configurarDatePicker() {
        val campoData = binding.activityFormularioPressaoData
        campoData.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "MATERIAl_DATE_PICKER")
            datePicker.addOnPositiveButtonClickListener { milli ->
                var dataSelecionada = milli.parseToLocaleDateTime()
                val agora = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                dataSelecionada = dataSelecionada.date.atTime(
                    hour = agora.hour,
                    minute = agora.minute
                )
                campoData.setText(dataSelecionada.toStringDateTimeBR())
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
            valor.stringtoLocalDateTime().toLong()
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
                    if (id == 0L) {
                        dao.adicionar(pressao)
                    } else {
                        dao.atualizar(pressao)
                    }
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
            id = id,
            data = data.stringtoLocalDateTime().toLong(),
            maxima = maxima,
            minima = minina,
            importado = importado
        )
    }
}