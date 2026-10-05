package com.vasquez.saborapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vasquez.saborapp.adapter.PlatoAdapter
import com.vasquez.saborapp.data.DBHelper
import com.vasquez.saborapp.data.PlatoDao
import com.vasquez.saborapp.databinding.ActivityPlatosBinding

class PlatosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlatosBinding
    private lateinit var platoDao: PlatoDao
    private lateinit var adapter: PlatoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlatosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        platoDao = PlatoDao(DBHelper(this))

        adapter = PlatoAdapter(emptyList())
        binding.rvPlatos.adapter = adapter

        binding.fabAgregarPlato.setOnClickListener {
            startActivity(Intent(this, PlatoFormActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        cargarPlatos()
    }

    private fun cargarPlatos() {
        val lista = platoDao.listar()
        adapter.actualizarLista(lista)
    }
}
