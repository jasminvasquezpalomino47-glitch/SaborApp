package com.vasquez.saborapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vasquez.saborapp.R
import com.vasquez.saborapp.databinding.ItemPlatoBinding
import com.vasquez.saborapp.model.Plato
import java.util.Locale

class PlatoAdapter(
    private var lista: List<Plato>
) : RecyclerView.Adapter<PlatoAdapter.PlatoViewHolder>() {

    class PlatoViewHolder(val binding: ItemPlatoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatoViewHolder {
        val binding = ItemPlatoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlatoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlatoViewHolder, position: Int) {
        val plato = lista[position]
        val context = holder.itemView.context

        holder.binding.tvNombrePlato.text = plato.nombre
        holder.binding.tvCategoriaPlato.text = plato.categoria
        holder.binding.tvPrecioPlato.text = String.format(Locale.getDefault(), "S/ %.2f", plato.precio)

        if (plato.disponible) {
            holder.binding.tvEstadoPlato.text = context.getString(R.string.estado_disponible)
            holder.binding.tvEstadoPlato.setTextColor(ContextCompat.getColor(context, R.color.verde_libre))
        } else {
            holder.binding.tvEstadoPlato.text = context.getString(R.string.estado_agotado)
            holder.binding.tvEstadoPlato.setTextColor(ContextCompat.getColor(context, R.color.rojo_ocupada))
        }
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nuevaLista: List<Plato>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}
