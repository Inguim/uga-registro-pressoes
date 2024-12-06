package com.example.registropressoes.ui.recyclerView.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.registropressoes.R
import com.example.registropressoes.databinding.PressaoItemBinding
import com.example.registropressoes.model.Pressao

class ListaPressoesAdapter(
    private val context: Context,
    pressoes: List<Pressao> = listOf(Pressao(maxima = 13.0, minima = 7.0))
) : RecyclerView.Adapter<ListaPressoesAdapter.ViewHolder>() {

    private val dataset = pressoes.toMutableList()

    inner class ViewHolder(private val binding: PressaoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var pressao: Pressao

        fun vincula(pressao: Pressao) {
            this.pressao = pressao
            with(binding) {
                pressaoItemData.text = pressao.dataToBr
                pressaoItemValor.text =
                    context.getString(
                        R.string.pressa_item_valor,
                        pressao.maxima.toString(),
                        pressao.minima.toString()
                    )
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

}