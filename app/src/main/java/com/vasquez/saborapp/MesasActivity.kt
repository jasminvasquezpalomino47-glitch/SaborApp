package com.vasquez.saborapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vasquez.saborapp.databinding.ActivityMesasBinding

class MesasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMesasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMesasBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
