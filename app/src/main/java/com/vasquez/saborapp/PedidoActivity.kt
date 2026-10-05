package com.vasquez.saborapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vasquez.saborapp.databinding.ActivityPedidoBinding

class PedidoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedidoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
