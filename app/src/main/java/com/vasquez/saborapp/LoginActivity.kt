package com.vasquez.saborapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.vasquez.saborapp.data.DBHelper
import com.vasquez.saborapp.data.UsuarioDao
import com.vasquez.saborapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var usuarioDao: UsuarioDao

    companion object {
        const val PREFS_NAME = "saborapp_prefs"
        const val KEY_USUARIO = "session_usuario"
        const val KEY_ROL = "session_rol"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // HU-12: Verificar sesión recordada
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val savedUser = prefs.getString(KEY_USUARIO, null)
        val savedRol = prefs.getString(KEY_ROL, null)

        if (!savedUser.isNullOrEmpty() && !savedRol.isNullOrEmpty()) {
            irAlMenu(savedUser, savedRol)
            return
        }

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
            // HU-12: Guardar sesión en SharedPreferences
            val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            prefs.edit {
                putString(KEY_USUARIO, user.usuario)
                putString(KEY_ROL, user.rol)
            }

            irAlMenu(user.usuario, user.rol)
        }
    }

    private fun irAlMenu(usuario: String, rol: String) {
        val intent = Intent(this, MenuActivity::class.java).apply {
            putExtra("usuario", usuario)
            putExtra("rol", rol)
        }
        startActivity(intent)
        finish()
    }
}
