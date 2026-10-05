package com.vasquez.saborapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vasquez.saborapp.databinding.ItemDetallePedidoBinding
import com.vasquez.saborapp.model.DetallePedido
import java.util.Locale

class DetalleAdapter(
    private var lista: List<DetallePedido>,
    private val mostrarBotonEliminar: Boolean = true,
    private val onEliminarClick: ((DetallePedido) -> Unit)? = null
) : RecyclerView.Adapter<DetalleAdapter.DetalleViewHolder>() {

    class DetalleViewHolder(val binding: ItemDetallePedidoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetalleViewHolder {
        val binding = ItemDetallePedidoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetalleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetalleViewHolder, position: Int) {
        val item = lista[position]

        holder.binding.tvCantidad.text = "${item.cantidad}x"
        holder.binding.tvNombrePlato.text = item.nombrePlato
        holder.binding.tvPrecioUnit.text = String.format(Locale.getDefault(), "S/ %.2f c/u", item.precioUnit)
        holder.binding.tvSubtotal.text = String.format(Locale.getDefault(), "S/ %.2f", item.subtotal)

        if (mostrarBotonEliminar) {
            holder.binding.btnEliminarItem.visibility = View.VISIBLE
            holder.binding.btnEliminarItem.setOnClickListener { onEliminarClick?.invoke(item) }
        } else {
            holder.binding.btnEliminarItem.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nuevaLista: List<DetallePedido>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}
