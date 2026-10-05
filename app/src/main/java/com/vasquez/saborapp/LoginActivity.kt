package com.vasquez.saborapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vasquez.saborapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIngresar.setOnClickListener { validar() }
    }

    private fun validar() {
        val usuario = binding.etUsuario.text.toString().trim()
        val clave = binding.etClave.text.toString().trim()

        binding.tilUsuario.error = null
        binding.tilClave.error = null

        var valido = true
        if (usuario.isEmpty()) {
            binding.tilUsuario.error = getString(R.string.error_campo_vacio)
            valido = false
        }
        if (clave.isEmpty()) {
            binding.tilClave.error = getString(R.string.error_campo_vacio)
            valido = false
        }
        if (!valido) return

        // Provisional (Sprint 1): En el Sprint 2 se valida con SQLite
        val rol = when {
            usuario == "admin" && clave == "1234" -> "ADMIN"
            usuario == "mozo" && clave == "1234" -> "MOZO"
            else -> null
        }

        if (rol == null) {
            Toast.makeText(this, R.string.error_credenciales, Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, MenuActivity::class.java).apply {
                putExtra("usuario", usuario)
                putExtra("rol", rol)
            }
            startActivity(intent)
            finish() // Para que "atrás" no vuelva al login
        }
    }
}
