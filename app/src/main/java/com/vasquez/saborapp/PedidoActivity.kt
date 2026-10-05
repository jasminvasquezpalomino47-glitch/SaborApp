package com.vasquez.saborapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.vasquez.saborapp.adapter.DetalleAdapter
import com.vasquez.saborapp.data.DBHelper
import com.vasquez.saborapp.data.MesaDao
import com.vasquez.saborapp.data.PedidoDao
import com.vasquez.saborapp.data.PlatoDao
import com.vasquez.saborapp.databinding.ActivityPedidoBinding
import com.vasquez.saborapp.model.Mesa
import com.vasquez.saborapp.model.Pedido
import com.vasquez.saborapp.model.Plato
import java.util.Locale

class PedidoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedidoBinding
    private lateinit var mesaDao: MesaDao
    private lateinit var platoDao: PlatoDao
    private lateinit var pedidoDao: PedidoDao

    private lateinit var adapter: DetalleAdapter
    private var listaMesas = listOf<Mesa>()
    private var listaPlatos = listOf<Plato>()

    private var mesaSeleccionada: Mesa? = null
    private var pedidoActual: Pedido? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper(this)
        mesaDao = MesaDao(dbHelper)
        platoDao = PlatoDao(dbHelper)
        pedidoDao = PedidoDao(dbHelper)

        adapter = DetalleAdapter(emptyList(), mostrarBotonEliminar = true) { item ->
            pedidoActual?.let { ped ->
                pedidoDao.eliminarDetalle(item.id, ped.id)
                cargarDetallesPedido()
            }
        }
        binding.rvDetallePedido.adapter = adapter

        binding.btnAgregarPlato.setOnClickListener { agregarPlato() }
        binding.btnVerCuenta.setOnClickListener {
            val mesa = mesaSeleccionada ?: return@setOnClickListener
            val ped = pedidoActual ?: return@setOnClickListener
            val intent = Intent(this, CuentaActivity::class.java).apply {
                putExtra("idMesa", mesa.id)
                putExtra("numeroMesa", mesa.numero)
                putExtra("idPedido", ped.id)
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarMesas()
        cargarPlatos()
    }

    private fun cargarMesas() {
        listaMesas = mesaDao.listar()
        val nombresMesas = listaMesas.map { "Mesa #${it.numero} (${it.estado})" }
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nombresMesas)
        binding.spMesas.adapter = adapterSpinner

        binding.spMesas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position in listaMesas.indices) {
                    seleccionarMesa(listaMesas[position])
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun cargarPlatos() {
        listaPlatos = platoDao.listarDisponibles()
        val nombresPlatos = listaPlatos.map { "${it.nombre} - S/ ${String.format(Locale.getDefault(), "%.2f", it.precio)}" }
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nombresPlatos)
        binding.spPlatos.adapter = adapterSpinner
    }

    private fun seleccionarMesa(mesa: Mesa) {
        mesaSeleccionada = mesa
        binding.layoutPanelPedido.visibility = View.VISIBLE
        binding.tvHeaderPedido.text = getString(R.string.label_pedido_mesa, mesa.numero, mesa.estado)

        // Obtener o crear pedido abierto para la mesa
        pedidoActual = pedidoDao.obtenerOCrearPedidoAbierto(mesa.id)
        cargarDetallesPedido()
    }

    private fun cargarDetallesPedido() {
        val ped = pedidoActual ?: return
        val detalles = pedidoDao.listarDetalles(ped.id)
        adapter.actualizarLista(detalles)

        val total = detalles.sumOf { it.subtotal }
        binding.tvTotalPedido.text = getString(R.string.label_total, total)
    }

    private fun agregarPlato() {
        val mesa = mesaSeleccionada ?: return
        val ped = pedidoActual ?: return

        val platoPos = binding.spPlatos.selectedItemPosition
        if (platoPos !in listaPlatos.indices) return

        val plato = listaPlatos[platoPos]
        val cantStr = binding.etCantidad.text.toString().trim()
        val cantidad = cantStr.toIntOrNull()

        if (cantidad == null || cantidad <= 0) {
            binding.tilCantidad.error = getString(R.string.error_cantidad_invalida)
            return
        }
        binding.tilCantidad.error = null

        val exito = pedidoDao.agregarPlatoAPedido(
            idPedido = ped.id,
            idMesa = mesa.id,
            idPlato = plato.id,
            cantidad = cantidad,
            precioUnit = plato.precio
        )

        if (exito) {
            binding.etCantidad.setText("1")
            cargarDetallesPedido()
            // Recargar mesa para actualizar su estado a OCUPADA
            val mesaActualizada = mesaDao.obtenerPorId(mesa.id)
            if (mesaActualizada != null) {
                mesaSeleccionada = mesaActualizada
                binding.tvHeaderPedido.text = getString(R.string.label_pedido_mesa, mesaActualizada.numero, mesaActualizada.estado)
            }
        }
    }
}
