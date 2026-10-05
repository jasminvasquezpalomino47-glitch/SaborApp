package com.vasquez.saborapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vasquez.saborapp.databinding.ActivityReportesBinding

class ReportesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
