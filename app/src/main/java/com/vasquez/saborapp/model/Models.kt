package com.vasquez.saborapp.model

data class Usuario(
    val id: Int = 0,
    val usuario: String,
    val clave: String,
    val rol: String
)

data class Plato(
    val id: Int = 0,
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val disponible: Boolean = true
)

data class Mesa(
    val id: Int = 0,
    val numero: Int,
    val capacidad: Int,
    val estado: String = "LIBRE"
)
