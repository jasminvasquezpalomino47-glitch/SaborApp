package com.vasquez.saborapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vasquez.saborapp.data.DBHelper
import com.vasquez.saborapp.data.PlatoDao
import com.vasquez.saborapp.databinding.ActivityPlatoFormBinding
import com.vasquez.saborapp.model.Plato

class PlatoFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlatoFormBinding
    private lateinit var platoDao: PlatoDao

    private val categorias = arrayOf("Entradas", "Fondos", "Bebidas", "Postres")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlatoFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        platoDao = PlatoDao(DBHelper(this))

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)
        binding.spCategoria.adapter = adapter

        binding.btnGuardar.setOnClickListener { guardar() }
    }

    private fun guardar() {
        val nombre = binding.etNombre.text.toString().trim()
        val precioStr = binding.etPrecio.text.toString().trim()
        val categoria = binding.spCategoria.selectedItem.toString()
        val disponible = binding.swDisponible.isChecked

        binding.tilNombre.error = null
        binding.tilPrecio.error = null

        var valido = true
        if (nombre.isEmpty()) {
            binding.tilNombre.error = getString(R.string.error_campo_vacio)
            valido = false
        }

        val precio = precioStr.toDoubleOrNull()
        if (precio == null || precio <= 0) {
            Toast.makeText(this, R.string.error_precio_invalido, Toast.LENGTH_SHORT).show()
            valido = false
        }

        if (!valido) return

        val plato = Plato(
            nombre = nombre,
            categoria = categoria,
            precio = precio!!,
            disponible = disponible
        )

        val res = platoDao.insertar(plato)
        if (res > 0) {
            Toast.makeText(this, R.string.msg_plato_guardado, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
