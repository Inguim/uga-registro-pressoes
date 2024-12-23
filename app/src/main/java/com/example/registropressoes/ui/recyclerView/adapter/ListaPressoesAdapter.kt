package com.example.registropressoes.ui.recyclerView.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.registropressoes.R
import com.example.registropressoes.databinding.PressaoItemBinding
import com.example.registropressoes.model.Pressao
import com.example.registropressoes.ui.utils.generateMedicaoIndicador

class ListaPressoesAdapter(
    private val context: Context,
    pressoes: List<Pressao> = emptyList(),
    var itemClickExcluir: (pressao: Pressao) -> Unit = {},
    var itemClickEditar: (pressao: Pressao) -> Unit = {},
) : RecyclerView.Adapter<ListaPressoesAdapter.ViewHolder>() {

    private val dataset = pressoes.toMutableList()

    inner class ViewHolder(private val binding: PressaoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var pressao: Pressao

        init {
            adicionarItemViewClick()
        }

        fun vincula(pressao: Pressao) {
            this.pressao = pressao
            with(binding) {
                pressaoItemData.text = pressao.dataToBr
                val (cor, texto) = generateMedicaoIndicador(context, pressao)
                pressaoItemValor.text = texto
                pressaoItemValor.setTextColor(cor)
            }
        }

        private fun adicionarItemViewClick() {
            binding.pressaoItemButtonRemover.setOnClickListener {
                with(context) {
                    AlertDialog.Builder(context)
                        .setTitle(getString(R.string.confirm_remocao))
                        .setMessage(getString(R.string.confirm_remocao_pressao))
                        .setPositiveButton(getString(R.string.confirm_remocao_accept)) { _, _ ->
                            itemClickExcluir(pressao)
                        }
                        .setNegativeButton(getString(R.string.confirm_remocao_cancel), null)
                        .show()
                }
            }
            binding.pressaoItemButtonEditar.setOnClickListener {
                itemClickEditar(pressao)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = PressaoItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.vincula(dataset[position])
    }

    override fun getItemCount(): Int = dataset.size

    @SuppressLint("NotifyDataSetChanged")
    fun atualizar(pressoes: List<Pressao>) {
        this.dataset.clear()
        this.dataset.addAll(pressoes)
        notifyDataSetChanged()
    }
}