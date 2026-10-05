package com.vasquez.saborapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vasquez.saborapp.adapter.DetalleAdapter
import com.vasquez.saborapp.data.DBHelper
import com.vasquez.saborapp.data.PedidoDao
import com.vasquez.saborapp.databinding.ActivityCuentaBinding
import com.vasquez.saborapp.model.DetallePedido
import java.util.Locale

class CuentaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCuentaBinding
    private lateinit var pedidoDao: PedidoDao

    private var idMesa: Int = -1
    private var numeroMesa: Int = -1
    private var idPedido: Int = -1

    private var detallesPedido = listOf<DetallePedido>()
    private var totalCuenta = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pedidoDao = PedidoDao(DBHelper(this))

        idMesa = intent.getIntExtra("idMesa", -1)
        numeroMesa = intent.getIntExtra("numeroMesa", -1)
        idPedido = intent.getIntExtra("idPedido", -1)

        binding.tvTituloCuenta.text = getString(R.string.titulo_cuenta, numeroMesa)

        cargarCuenta()

        binding.btnCerrarCuenta.setOnClickListener { cerrarCuenta() }
        binding.btnCompartirWhatsApp.setOnClickListener { compartirPorWhatsApp() }
    }

    private fun cargarCuenta() {
        if (idPedido == -1) return
        detallesPedido = pedidoDao.listarDetalles(idPedido)
        totalCuenta = detallesPedido.sumOf { it.subtotal }

        val adapter = DetalleAdapter(detallesPedido, mostrarBotonEliminar = false)
        binding.rvCuentaDetalle.adapter = adapter
        binding.tvTotalCuenta.text = getString(R.string.label_total, totalCuenta)
    }

    private fun cerrarCuenta() {
        if (idPedido == -1 || idMesa == -1) return

        val exito = pedidoDao.cerrarCuenta(idPedido, idMesa)
        if (exito) {
            Toast.makeText(this, R.string.msg_cuenta_cerrada, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun compartirPorWhatsApp() {
        if (detallesPedido.isEmpty()) return

        val sb = StringBuilder()
        sb.append("*** POLLERÍA EL BUEN SABOR ***\n")
        sb.append("Cuenta Mesa #$numeroMesa\n")
        sb.append("------------------------------\n")
        for (item in detallesPedido) {
            sb.append("${item.cantidad}x ${item.nombrePlato} - S/ ${String.format(Locale.getDefault(), "%.2f", item.subtotal)}\n")
        }
        sb.append("------------------------------\n")
        sb.append("TOTAL A PAGAR: S/ ${String.format(Locale.getDefault(), "%.2f", totalCuenta)}\n")
        sb.append("¡Gracias por su preferencia!")

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, sb.toString())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Compartir cuenta por...")
        startActivity(shareIntent)
    }
}
