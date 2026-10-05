package com.vasquez.saborapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vasquez.saborapp.R
import com.vasquez.saborapp.databinding.ItemMesaBinding
import com.vasquez.saborapp.model.Mesa

class MesaAdapter(
    private var lista: List<Mesa>,
    private val onItemClick: ((Mesa) -> Unit)? = null
) : RecyclerView.Adapter<MesaAdapter.MesaViewHolder>() {

    class MesaViewHolder(val binding: ItemMesaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MesaViewHolder {
        val binding = ItemMesaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MesaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MesaViewHolder, position: Int) {
        val mesa = lista[position]
        val context = holder.itemView.context

        holder.binding.tvNumeroMesa.text = context.getString(R.string.fmt_numero_mesa, mesa.numero)
        holder.binding.tvCapacidadMesa.text = context.getString(R.string.fmt_capacidad_mesa, mesa.capacidad)
        holder.binding.tvEstadoMesa.text = mesa.estado

        if (mesa.estado == "LIBRE") {
            holder.binding.tvEstadoMesa.setTextColor(ContextCompat.getColor(context, R.color.verde_libre))
            holder.binding.tvEstadoMesa.setBackgroundColor(ContextCompat.getColor(context, R.color.verde_superficie))
            holder.binding.cardMesa.strokeColor = ContextCompat.getColor(context, R.color.verde_libre)
        } else {
            holder.binding.tvEstadoMesa.setTextColor(ContextCompat.getColor(context, R.color.rojo_ocupada))
            holder.binding.tvEstadoMesa.setBackgroundColor(ContextCompat.getColor(context, R.color.rojo_superficie))
            holder.binding.cardMesa.strokeColor = ContextCompat.getColor(context, R.color.rojo_ocupada)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(mesa)
        }
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nuevaLista: List<Mesa>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}
