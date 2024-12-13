package com.example.registropressoes.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.database.AppDataBase
import com.example.registropressoes.R
import com.example.registropressoes.databinding.ActivityListaPressoesBinding
import com.example.registropressoes.extensions.parseToUmaCasaDecimal
import com.example.registropressoes.extensions.setIconColor
import com.example.registropressoes.extensions.setTitleColor
import com.example.registropressoes.model.PressaoFiltro
import com.example.registropressoes.ui.recyclerView.adapter.ListaPressoesAdapter
import kotlinx.coroutines.launch

class ListaPressoesActivity : AppCompatActivity() {
    private val adapter = ListaPressoesAdapter(context = this)
    private val binding by lazy {
        ActivityListaPressoesBinding.inflate(layoutInflater)
    }
    private val dao by lazy {
        AppDataBase.instancia(this).pressaoDAO()
    }
    private val filtro = PressaoFiltro()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configurarRecyclerView()
        configurarFab()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            launch {
                buscarPressoes()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_pressoes, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_lista_pressoes_importar -> goFormularioImportacao()
            R.id.menu_lista_pressoes_exportar -> goFormularioExportacao()
            else -> filtrar(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        indicarMenuSelecionadoFiltro(menu)
        return super.onPrepareOptionsMenu(menu)
    }

    private fun indicarMenuSelecionadoFiltro(menu: Menu?) {
        menu?.let {
            val item: MenuItem = it.findItem(R.id.menu_lista_pressoes_filtrar)
            val subMenu = item.subMenu
            val itemFiltrando: MenuItem? = when (filtro.mode) {
                EnumFiltrosPressao.HOJE ->
                    subMenu?.findItem(R.id.menu_lista_pressoes_filtrar_hoje)

                EnumFiltrosPressao.SEMANA ->
                    subMenu?.findItem(R.id.menu_lista_pressoes_filtrar_semana)

                EnumFiltrosPressao.MES ->
                    subMenu?.findItem(R.id.menu_lista_pressoes_filtrar_mes)

                EnumFiltrosPressao.TODOS ->
                    subMenu?.findItem(R.id.menu_lista_pressoes_filtrar_todos)

                EnumFiltrosPressao.IMPORTADOS ->
                    subMenu?.findItem(R.id.menu_lista_pressoes_filtrar_importados)
            }
            when (filtro.mode) {
                EnumFiltrosPressao.TODOS -> item.setIcon(R.drawable.ic_action_filter_off)
                else -> item.setIcon(R.drawable.ic_action_filter_on)
            }
            item.setIconColor(this, R.color.colorMenuItemSelected)
            item.setTitleColor(this, R.color.colorMenuItemSelected)
            itemFiltrando?.let {
                itemFiltrando.setIconColor(this, R.color.colorMenuItemSelected)
                itemFiltrando.setTitleColor(this, R.color.colorMenuItemSelected)
            }
        }
    }

    private fun filtrar(item: MenuItem) {
        var buscar = true
        when (item.itemId) {
            R.id.menu_lista_pressoes_filtrar_hoje -> filtro.definir(EnumFiltrosPressao.HOJE)
            R.id.menu_lista_pressoes_filtrar_semana -> filtro.definir(EnumFiltrosPressao.SEMANA)
            R.id.menu_lista_pressoes_filtrar_mes -> filtro.definir(EnumFiltrosPressao.MES)
            R.id.menu_lista_pressoes_filtrar_todos -> filtro.definir(EnumFiltrosPressao.TODOS)
            R.id.menu_lista_pressoes_filtrar_importados -> filtro.definir(EnumFiltrosPressao.IMPORTADOS)
            else -> buscar = false
        }
        if (buscar) {
            lifecycleScope.launch {
                launch {
                    buscarPressoes()
                }
            }
        }
        invalidateOptionsMenu()
    }

    private suspend fun buscarIndicadores() {
        val (inicio, fim) = filtro.getPeriodo()
        val importado: Boolean? = if (filtro.mode == EnumFiltrosPressao.IMPORTADOS) {
            true
        } else {
            null
        }
        val media = dao.listarMediaMedicoes(inicio, fim, importado)
        val maxima = dao.listarMaiorMedicao(inicio, fim, importado)
        val minima = dao.listarMenorMedicao(inicio, fim, importado)
        with(binding) {
            cardIndicadoresMedia.text = getString(
                R.string.card_indicadores_media,
                media.avg_maxima.parseToUmaCasaDecimal(),
                media.avg_minima.parseToUmaCasaDecimal()
            )
            cardIndicadoresMaxima.text = maxima?.let {
                getString(
                    R.string.card_indicadores_maxima,
                    it.maxima.parseToUmaCasaDecimal(),
                    it.minima.parseToUmaCasaDecimal(),
                    "(${it.dataToBr})"
                )
            } ?: getString(R.string.card_indicadores_maxima, "0", "0", "")
            cardIndicadoresMinima.text = minima?.let {
                getString(
                    R.string.card_indicadores_minima,
                    it.maxima.parseToUmaCasaDecimal(),
                    it.minima.parseToUmaCasaDecimal(),
                    "(${it.dataToBr})"
                )
            } ?: getString(R.string.card_indicadores_minima, "0", "0", "")
        }
    }

    private fun configurarRecyclerView() {
        val recyclerView = binding.activityListaPressoesRecyclerView
        recyclerView.adapter = adapter
        configurarBotaoRemover()
        configurarBotaoEditar()
    }

    private fun configurarBotaoRemover() {
        adapter.itemClickExcluir = {
            it.let {
                lifecycleScope.launch {
                    dao.remover(it)
                    buscarPressoes()
                }
            }
        }
    }

    private fun configurarBotaoEditar() {
        adapter.itemClickEditar = {
            Intent(this, FormularioPressaoActivity::class.java).apply {
                putExtra(CHAVE_PRESSAO_ID, it.id)
                startActivity(this)
            }
        }
    }

    private suspend fun buscarPressoes(atualizarIndicadores: Boolean = true) {
        if (filtro.mode == EnumFiltrosPressao.TODOS) {
            dao.listar().collect {
                if (atualizarIndicadores) buscarIndicadores()
                adapter.atualizar(it)
            }
        } else if (filtro.mode == EnumFiltrosPressao.IMPORTADOS) {
            dao.listarImportados().collect {
                if (atualizarIndicadores) buscarIndicadores()
                adapter.atualizar(it)
            }
        } else {
            val (start, end) = filtro.getPeriodo()
            if (start != null && end != null) {
                dao.listarPeriodo(start, end).collect {
                    if (atualizarIndicadores) buscarIndicadores()
                    adapter.atualizar(it)
                }
            }
        }
    }

    private fun configurarFab() {
        val fab = binding.activityListaPressoesFab
        fab.setOnClickListener {
            goFormularioPressoes()
        }
    }

    private fun goFormularioPressoes() {
        val intent = Intent(this, FormularioPressaoActivity::class.java)
        startActivity(intent)
    }

    private fun goFormularioImportacao() {
        val intent = Intent(this, FormularioImportarMedicoesActivity::class.java)
        startActivity(intent)
    }

    private fun goFormularioExportacao() {
        val intent = Intent(this, ExportarActivity::class.java)
        startActivity(intent)
    }
}
