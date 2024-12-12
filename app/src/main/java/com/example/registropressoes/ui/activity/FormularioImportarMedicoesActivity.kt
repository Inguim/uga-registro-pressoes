package com.example.registropressoes.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.registropressoes.databinding.ActivityImportarMedicoesBinding

class FormularioImportarMedicoesActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityImportarMedicoesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}