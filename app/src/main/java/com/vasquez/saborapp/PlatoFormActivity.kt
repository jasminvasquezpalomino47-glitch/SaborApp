package com.vasquez.saborapp

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.vasquez.saborapp.data.DBHelper
import com.vasquez.saborapp.data.PlatoDao
import com.vasquez.saborapp.databinding.ActivityPlatoFormBinding
import com.vasquez.saborapp.model.Plato

class PlatoFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlatoFormBinding
    private lateinit var platoDao: PlatoDao
    private var platoId: Int = -1

    private val categorias = arrayOf("Entradas", "Fondos", "Bebidas", "Postres")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlatoFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        platoDao = PlatoDao(DBHelper(this))

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)
        binding.spCategoria.adapter = adapter

        platoId = intent.getIntExtra("platoId", -1)

        if (platoId != -1) {
            // Modo edición
            binding.tvTituloForm.text = getString(R.string.titulo_editar_plato)
            binding.btnGuardar.text = getString(R.string.btn_actualizar)
            binding.btnEliminar.visibility = View.VISIBLE
            cargarPlato()
        }

        binding.btnGuardar.setOnClickListener { guardar() }
        binding.btnEliminar.setOnClickListener { confirmarEliminacion() }
    }

    private fun cargarPlato() {
        val plato = platoDao.obtenerPorId(platoId) ?: return
        binding.etNombre.setText(plato.nombre)
        binding.etPrecio.setText(plato.precio.toString())
        binding.swDisponible.isChecked = plato.disponible

        val catIndex = categorias.indexOf(plato.categoria)
        if (catIndex >= 0) {
            binding.spCategoria.setSelection(catIndex)
        }
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
            binding.tilPrecio.error = getString(R.string.error_precio_invalido)
            valido = false
        }

        if (!valido) return

        val plato = Plato(
            id = if (platoId != -1) platoId else 0,
            nombre = nombre,
            categoria = categoria,
            precio = precio!!,
            disponible = disponible
        )

        if (platoId == -1) {
            val res = platoDao.insertar(plato)
            if (res > 0) {
                Toast.makeText(this, R.string.msg_plato_guardado, Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            val res = platoDao.actualizar(plato)
            if (res > 0) {
                Toast.makeText(this, R.string.msg_plato_actualizado, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun confirmarEliminacion() {
        AlertDialog.Builder(this)
            .setTitle(R.string.btn_eliminar)
            .setMessage(R.string.confirm_eliminar_plato)
            .setPositiveButton(R.string.btn_aceptar) { _, _ -> eliminar() }
            .setNegativeButton(R.string.btn_cancelar, null)
            .show()
    }

    private fun eliminar() {
        try {
            val res = platoDao.eliminar(platoId)
            if (res) {
                Toast.makeText(this, R.string.msg_plato_eliminado, Toast.LENGTH_SHORT).show()
                finish()
            }
        } catch (e: SQLiteConstraintException) {
            Toast.makeText(this, R.string.error_eliminar_plato_pedidos, Toast.LENGTH_LONG).show()
        }
    }
}
