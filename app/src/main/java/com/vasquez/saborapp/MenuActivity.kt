package com.vasquez.saborapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.vasquez.saborapp.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usuario = intent.getStringExtra("usuario") ?: ""
        val rol = intent.getStringExtra("rol") ?: ""

        binding.tvBienvenida.text = getString(R.string.menu_bienvenida, usuario, rol)

        // HU-02 (CA4): Reportes solo visible para ADMIN
        if (rol != "ADMIN") {
            binding.btnReportes.visibility = View.GONE
        }

        binding.btnPlatos.setOnClickListener {
            startActivity(Intent(this, PlatosActivity::class.java))
        }
        binding.btnMesas.setOnClickListener {
            startActivity(Intent(this, MesasActivity::class.java))
        }
        binding.btnPedidos.setOnClickListener {
            startActivity(Intent(this, PedidoActivity::class.java))
        }
        binding.btnReportes.setOnClickListener {
            startActivity(Intent(this, ReportesActivity::class.java))
        }

        binding.btnSalir.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
}
