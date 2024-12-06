package com.example.registropressoes.ui.activity

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
    private val pressaoDAO by lazy {
        AppDataBase.instancia(this).pressaoDAO()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            launch {
                buscarPressoes()
            }
        }
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.activityListaPressoesRecyclerView
        recyclerView.adapter = adapter
    }

    private suspend fun buscarPressoes() {
        pressaoDAO.listar().collect {
            adapter.atualizar(it)
        }
    }
}
