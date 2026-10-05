package com.vasquez.saborapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vasquez.saborapp.data.DBHelper
import com.vasquez.saborapp.data.ReporteDao
import com.vasquez.saborapp.databinding.ActivityReportesBinding
import java.util.Locale

class ReportesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportesBinding
    private lateinit var reporteDao: ReporteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reporteDao = ReporteDao(DBHelper(this))

        cargarReportes()
    }

    private fun cargarReportes() {
        val totalHoy = reporteDao.ventaDelDia()
        val pedidosCount = reporteDao.pedidosCerradosHoy()

        if (pedidosCount == 0) {
            binding.tvMontoTotalHoy.text = getString(R.string.label_sin_ventas)
            binding.tvPedidosHoyCount.text = ""
        } else {
            binding.tvMontoTotalHoy.text = String.format(Locale.getDefault(), "S/ %.2f", totalHoy)
            binding.tvPedidosHoyCount.text = getString(R.string.label_pedidos_cerrados_count, pedidosCount)
        }

        // Top 5 Platos
        val topPlatos = reporteDao.topPlatos()
        if (topPlatos.isEmpty()) {
            binding.tvTopPlatos.text = getString(R.string.label_sin_ventas)
        } else {
            val sb = StringBuilder()
            topPlatos.forEachIndexed { index, p ->
                sb.append("${index + 1}. ${p.nombrePlato} — ${p.cantidadTotal} und (S/ ${String.format(Locale.getDefault(), "%.2f", p.ventaTotal)})\n")
            }
            binding.tvTopPlatos.text = sb.toString().trim()
        }

        // Ventas por Mesa
        val ventasMesa = reporteDao.ventaPorMesa()
        if (ventasMesa.isEmpty()) {
            binding.tvVentasPorMesa.text = getString(R.string.label_sin_ventas)
        } else {
            val sb = StringBuilder()
            ventasMesa.forEach { m ->
                sb.append("• Mesa #${m.numeroMesa}: S/ ${String.format(Locale.getDefault(), "%.2f", m.ventaTotal)}\n")
            }
            binding.tvVentasPorMesa.text = sb.toString().trim()
        }
    }
}
