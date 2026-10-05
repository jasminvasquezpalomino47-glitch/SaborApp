package com.vasquez.saborapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.vasquez.saborapp.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE)
        val usuario = intent.getStringExtra("usuario") ?: prefs.getString(LoginActivity.KEY_USUARIO, "") ?: ""
        val rol = intent.getStringExtra("rol") ?: prefs.getString(LoginActivity.KEY_ROL, "") ?: ""

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

        // HU-12: Borrar sesión al salir
        binding.btnSalir.setOnClickListener {
            prefs.edit { clear() }
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}
