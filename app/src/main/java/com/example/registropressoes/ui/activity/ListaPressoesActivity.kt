package com.example.registropressoes.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.database.AppDataBase
import com.example.registropressoes.databinding.ActivityListaPressoesBinding
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
        dao.listar().collect {
            adapter.atualizar(it)
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
