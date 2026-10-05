package com.vasquez.saborapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vasquez.saborapp.data.DBHelper
import com.vasquez.saborapp.data.UsuarioDao
import com.vasquez.saborapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var usuarioDao: UsuarioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper(this)
        usuarioDao = UsuarioDao(dbHelper)

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

        // HU-04: Validar usuario con consulta parametrizada desde SQLite
        val user = usuarioDao.validarUsuario(usuario, clave)

        if (user == null) {
            Toast.makeText(this, R.string.error_credenciales, Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, MenuActivity::class.java).apply {
                putExtra("usuario", user.usuario)
                putExtra("rol", user.rol)
            }
            startActivity(intent)
            finish() // Para que "atrás" no vuelva al login
        }
    }
}
