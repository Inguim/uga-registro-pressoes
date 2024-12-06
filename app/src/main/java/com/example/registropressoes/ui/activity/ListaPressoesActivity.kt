package com.example.registropressoes.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.registropressoes.databinding.ActivityListaPressoesBinding
import com.example.registropressoes.ui.recyclerView.adapter.ListaPressoesAdapter

class ListaPressoesActivity : AppCompatActivity() {
    private val adapter = ListaPressoesAdapter(context = this)
    private val binding by lazy {
        ActivityListaPressoesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.activityListaPressoesRecyclerView
        recyclerView.adapter = adapter
    }
}
