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
        filtrar(item)
        return super.onOptionsItemSelected(item)
    }

    private fun filtrar(item: MenuItem) {
        when (item.itemId) {
            R.id.menu_lista_pressoes_filtrar_hoje -> filtro.definir(EnumFiltrosPressao.HOJE)
            R.id.menu_lista_pressoes_filtrar_semana -> filtro.definir(EnumFiltrosPressao.SEMANA)
            R.id.menu_lista_pressoes_filtrar_mes -> filtro.definir(EnumFiltrosPressao.MES)
            R.id.menu_lista_pressoes_filtrar_todos -> filtro.definir(EnumFiltrosPressao.TODOS)
        }
        lifecycleScope.launch {
            buscarPressoes()
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

    private suspend fun buscarPressoes() {
        if (filtro.mode == EnumFiltrosPressao.TODOS) {
            dao.listar().collect {
                adapter.atualizar(it)
            }
        } else {
            val (start, end) = filtro.getPeriodo()
            if (start != null && end != null) {
                dao.listarPeriodo(start, end).collect {
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
}
