package com.vasquez.saborapp

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.vasquez.saborapp.adapter.MesaAdapter
import com.vasquez.saborapp.data.DBHelper
import com.vasquez.saborapp.data.MesaDao
import com.vasquez.saborapp.databinding.ActivityMesasBinding
import com.vasquez.saborapp.databinding.DialogAgregarMesaBinding
import com.vasquez.saborapp.model.Mesa

class MesasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMesasBinding
    private lateinit var mesaDao: MesaDao
    private lateinit var adapter: MesaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMesasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mesaDao = MesaDao(DBHelper(this))

        adapter = MesaAdapter(emptyList())
        binding.rvMesas.adapter = adapter

        binding.fabAgregarMesa.setOnClickListener { mostrarDialogoAgregar() }

        cargarMesas()
    }

    private fun cargarMesas() {
        val lista = mesaDao.listar()
        adapter.actualizarLista(lista)
    }

    private fun mostrarDialogoAgregar() {
        val dialogBinding = DialogAgregarMesaBinding.inflate(layoutInflater)

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.btn_guardar) { dialog, _ ->
                val numStr = dialogBinding.etNumeroMesa.text.toString().trim()
                val capStr = dialogBinding.etCapacidadMesa.text.toString().trim()

                val numero = numStr.toIntOrNull()
                val capacidad = capStr.toIntOrNull()

                if (numero == null || numero <= 0) {
                    Toast.makeText(this, R.string.error_campo_vacio, Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (capacidad == null || capacidad !in 1..12) {
                    Toast.makeText(this, R.string.error_capacidad_invalida, Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                try {
                    val res = mesaDao.insertar(Mesa(numero = numero, capacidad = capacidad))
                    if (res > 0) {
                        Toast.makeText(this, R.string.msg_mesa_guardada, Toast.LENGTH_SHORT).show()
                        cargarMesas()
                        dialog.dismiss()
                    }
                } catch (e: SQLiteConstraintException) {
                    Toast.makeText(this, R.string.error_mesa_existe, Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.btn_cancelar, null)
            .show()
    }
}
